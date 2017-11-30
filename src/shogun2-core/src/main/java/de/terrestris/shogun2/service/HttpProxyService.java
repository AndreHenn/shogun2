package de.terrestris.shogun2.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import de.terrestris.shogun2.util.http.HttpUtil;
import de.terrestris.shogun2.util.model.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


@Service("httpProxyService")
public class HttpProxyService {

	/**
	 * Logger instance for this class
	 */
	private static Logger LOGGER = Logger.getLogger(HttpProxyService.class);

	/* +--------------------------------------------------------------------+ */
	/* | Generic constants                                                  | */
	/* +--------------------------------------------------------------------+ */

	/**
	 * The path matcher to check if the subpath in a request matches the pattern
	 * that is configured for a service.
	 */
	private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

	/**
	 * Used to as content type for error messages if a request could not be
	 * proxied.
	 */
	private static final String CONTENT_TYPE_TEXT_PLAIN = MediaType.TEXT_PLAIN.toString();

	/* +--------------------------------------------------------------------+ */
	/* | static errors and response entities                                | */
	/* +--------------------------------------------------------------------+ */

	private static final String ERR_MSG_400_NO_URL = "ERROR 400 (Bad Request):"
			+ " The ServiceInterceptor could not determine a URL to proxy to.";

	private static final String ERR_MSG_400_COMMON = "ERROR 400 (Bad Request):"
			+ " Please let an admin have a look at the log files for details.";

	private static final String ERR_MSG_400_WRONG_PARAMETER = "ERROR 400 (Bad Request):"
			+ " The ServiceInterceptor could not determine a required parameter/value.";

	private static final String ERR_MSG_400_WRONG_PATH = "ERROR 400 (Bad Request):"
			+ " The request contains an invalid path.";

	private static final String ERR_MSG_401 = "ERROR 401 (Unauthorized):"
			+ " The requested service is protected and the client needs to authenticate.";

	private static final String ERR_MSG_403 = "ERROR 403 (Forbidden):"
			+ " The user is not authorized to access this resource.";

	private static final String ERR_MSG_404 = "ERROR 404 (Not found):"
			+ " The ServiceInterceptor could not find the requested service.";

	private static final String ERR_MSG_405 = "ERROR 405: (Method Not Allowed):"
			+ " The ServiceInterceptor does not support this request method";

	private static final String ERR_MSG_500 = "ERROR 502 (Internal Error)";

	private static final String ERR_MSG_502 = "ERROR 502 (Bad Gateway):"
			+ " The ServiceInterceptor does not allow you to access that location.";

	private static final String ERR_MSG_503 = "ERROR 503 (Service Unavailable):"
			+ " The requested service is currently not available.";

	private static final String ERR_MSG_505 = "ERROR 505 (HTTP Version Not Supported):"
			+ " The server does not support the HTTP protocol version used in the request."
			+ " This may be because the https certificate is untrusted (self-signed?).";

	private static final ResponseEntity<String> RESPONSE_400_BAD_REQUEST_COMMON =
			ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_400_COMMON);

	private static final ResponseEntity<String> RESPONSE_400_BAD_REQUEST_NO_URL =
			ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_400_NO_URL);

	private static final ResponseEntity<String> RESPONSE_400_BAD_REQUEST_WRONG_PARAMETER =
			ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_400_WRONG_PARAMETER);

	private static final ResponseEntity<String> RESPONSE_400_BAD_REQUEST_WRONG_PATH =
			ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_400_WRONG_PATH);

	/**
	 * This is not final as we need an injected property value for the WWW-Authenticate http header,
	 * so this object will be initialized in the init method.
	 */
	private static ResponseEntity<String> RESPONSE_401_UNAUTHORIZED;

	private static final ResponseEntity<String> RESPONSE_403_FORBIDDEN =
			ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_403);

	private static final ResponseEntity<String> RESPONSE_404_NOT_FOUND =
			ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_404);

	private static final ResponseEntity<String> RESPONSE_405_METHOD_NOT_ALLOWED =
			ResponseEntity
					.status(HttpStatus.METHOD_NOT_ALLOWED)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_405);

	private static final ResponseEntity<String> RESPONSE_500_INTERNAL_SERVER_ERROR =
			ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_500);

	private static final ResponseEntity<String> RESPONSE_502_BAD_GATEWAY =
			ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_502);

	private static final ResponseEntity<String> RESPONSE_503_SERVICE_UNAVAILABLE =
			ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_503);

	private static final ResponseEntity<String> RESPONSE_505_SSL_CERT_NOT_ACCEPTED =
			ResponseEntity
					.status(HttpStatus.HTTP_VERSION_NOT_SUPPORTED)
					.header("Content-Type", CONTENT_TYPE_TEXT_PLAIN)
					.body(ERR_MSG_505);

	private static final HttpResponseFactory HTTP_RESPONSE_FACTORY = new DefaultHttpResponseFactory();
	private static HttpResponse httpResponseCertNotAccepted = null;

	/* +--------------------------------------------------------------------+ */
	/* | HTTP header stuff                                                  | */
	/* +--------------------------------------------------------------------+ */
	/**
	 * Occurrences of the string {@value #UNQUOTED_SUBTYPE_GML} will be replaced
	 * with the quoted {@value #QUOTED_SUBTYPE_GML}.
	 */
	private static final String UNQUOTED_SUBTYPE_GML = " subtype=gml/3.1.1";

	/**
	 * This is the quoted replacement ({@value #QUOTED_SUBTYPE_GML}) for the
	 * string {@value #UNQUOTED_SUBTYPE_GML}.
	 */
	private static final String QUOTED_SUBTYPE_GML = " subtype=\"gml/3.1.1\"";

	/**
	 * Appended to {@link #JSON_CONTENT_TYPE_HEADER} and
	 * {@link #CSV_CONTENT_TYPE_HEADER}
	 */
	private static final String HEADER_SUFFIX_UTF8_CHARSET = "; charset=utf-8";

	/**
	 * A normalized Content-Type-header {@value #JSON_CONTENT_TYPE_HEADER} as
	 * replacement for content types that look like JSON (see
	 * {@link #CONTENT_TYPE_JSON_HINTS}).
	 */
	private static final String JSON_CONTENT_TYPE_HEADER = "application/json"
			+ HEADER_SUFFIX_UTF8_CHARSET;

	/**
	 * A normalized Content-Type-header {@value #CSV_CONTENT_TYPE_HEADER} as
	 * replacement for content types that look like CSV (see
	 * {@link #CONTENT_TYPE_CSV_HINTS}).
	 */
	private static final String CSV_CONTENT_TYPE_HEADER = "text/csv"
			+ HEADER_SUFFIX_UTF8_CHARSET;

	/**
	 * The set of Content-Type-headers strings which we'll replace with
	 * {@link #JSON_CONTENT_TYPE_HEADER}
	 */
	private static final Set<String> CONTENT_TYPE_JSON_HINTS = new HashSet<String>(
			Arrays.asList("json", "application/json", "text/json"));

	/**
	 * The set of Content-Type-headers strings which we'll replace with
	 * {@link #JSON_CONTENT_TYPE_HEADER}
	 */
	private static final Set<String> CONTENT_TYPE_CSV_HINTS = new HashSet<String>(
			Arrays.asList("text/csv"));

	/**
	 * The set of header names which we'll ignore when returning a response. The
	 * values herein are all lowercase.
	 */
	private static final Set<String> LC_UNSUPPORTED_HEADERS = new HashSet<String>(
			Arrays.asList("transfer-encoding"));

	/**
	 * @param url
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseEntity<?> doProxy(String url, HttpServletRequest request) {

		LOGGER.debug("Intercepting a request against URL '" + url+ "'");

		if (url == null || request == null) {
			LOGGER.warn(ERR_MSG_400_NO_URL);
			return RESPONSE_400_BAD_REQUEST_NO_URL;
		}

		// Check for a valid/matching path in the request
		// TODO
//		final String serviceRequestPath = extractPathFromPattern(request, serviceName);
//		String antPathPattern = service.getPathPattern();

		// fix pattern if necessary
//		if(antPathPattern == null || antPathPattern.isEmpty()) {
//			antPathPattern = "/**";
//		}
//
//		final String pathToMatch = serviceRequestPath.isEmpty() ? "/" : serviceRequestPath;
//		final boolean requestMatchesPathPattern = PATH_MATCHER.match(antPathPattern, pathToMatch);
//
//		if(!requestMatchesPathPattern) {
//			LOGGER.warn(ERR_MSG_400_WRONG_PATH);
//			return RESPONSE_400_BAD_REQUEST_WRONG_PATH;
//		}

		// Check for required parameters
		//Map<String, String> requiredParameters = service.getRequiredParameters();
//		if(requiredParameters != null && !requiredParameters.isEmpty()) {
//			// it seems that there are required parameters/values,
//			// so let's check if the request is valid
//			for (String parameter : requiredParameters.keySet()) {
//				// TODO become more smart here by supporting multiple values for a parameter
//				// (this will currently only work for single value parameters)
//				String parameterValue = request.getParameter(parameter);
//
//				if (parameterValue == null || !requiredParameters.get(parameter).equalsIgnoreCase(parameterValue)) {
//					// the parameter does not exist in the request OR the value
//					// exists, but does not equal the expected one
//					LOGGER.warn(ERR_MSG_400_WRONG_PARAMETER);
//					return RESPONSE_400_BAD_REQUEST_WRONG_PARAMETER;
//				}
//			}
//		}

		// Check if service is publicly available or not
//		if(service.isSecured()) {
//			String userAccountName = userService.getAccountNameOfLoggedInUser();
//
//			if (userAccountName == null || userAccountName.equals("anonymousUser") || userAccountName.isEmpty()) {
//				// there is no logged in user
//				LOGGER.warn(ERR_MSG_401);
//				return RESPONSE_401_UNAUTHORIZED;
//			}
//
//			// check if the user has the right
//			boolean userHasRightForService = serviceService.userHasRightForService(userAccountName, service);
//			LOGGER.debug("User '" + userAccountName + "' has right? -> " + userHasRightForService);
//
//			if(!userHasRightForService) {
//				LOGGER.warn(ERR_MSG_403);
//				return RESPONSE_403_FORBIDDEN;
//			}
//		}

		// finally proxy the request
		Response response = null;
//		String queryString = request.getQueryString();
//		if(queryString != null) {
//			// append query string if there are parameters
//			url += "?" + queryString;
//		}

//		boolean forwardAuth = service.isForwardAuthentication();
		boolean forwardAuth = false;

		if (HttpUtil.isHttpGetRequest(request)) {
			try {
				URI uri = new URI(url);
				LOGGER.debug("Forwarding as GET to uri: " + uri);
				response = HttpUtil.get(uri);
			} catch (URISyntaxException | HttpException e) {
				String errorMessage = "Error forwarding GET request: " + e.getMessage();
				LOGGER.error(errorMessage);
				return RESPONSE_400_BAD_REQUEST_COMMON;
			}
//		} else if (HttpUtil.isHttpPostRequest(request)) {
//			if (HttpUtil.isFormMultipartPost(request)) {
//				try {
//					LOGGER.debug("Forwarding as form/multipart POST");
//					//response = HttpUtil.post(uri, request, true, true, forwardAuth);
//				} catch (URISyntaxException | HttpException | IllegalStateException | IOException | ServletException e) {
//					String errorMessage = "Error forwarding form/multipart POST request: " + e.getMessage();
//					LOGGER.error(errorMessage);
//					return RESPONSE_400_BAD_REQUEST_COMMON;
//				}
//			} else {
//				try {
//					LOGGER.debug("Forwarding as POST");
//					response = HttpUtil.forwardPost(url, request, true, true, forwardAuth);
//				} catch (URISyntaxException | HttpException e) {
//					String errorMessage = "Error forwarding POST request: " + e.getMessage();
//					LOGGER.error(errorMessage);
//					return RESPONSE_400_BAD_REQUEST_COMMON;
//				}
//			}
		} else {
			LOGGER.error("HTTP method: " + request.getMethod() + " is not supported by this proxy.");
			return RESPONSE_405_METHOD_NOT_ALLOWED;
		}

		byte[] bytes = response.getBody();
		final HttpHeaders headersToForward = response.getHeaders(); //getHeadersToForward(response); // TODO adapt headers!?

		// LOG response headers
		Set<Entry<String, List<String>>> headerEntries = headersToForward.entrySet();
		for (Entry<String, List<String>> headerEntry : headerEntries) {
			String headerKey = headerEntry.getKey();
			List<String> headerValues = headerEntry.getValue();
			String joinedHeaderValues = StringUtils.join(headerValues, "; ");

			LOGGER.debug("Got the following response header: " + headerKey + "=" + joinedHeaderValues);
		}

		final HttpStatus responseHttpStatus = response.getStatusCode();

		return new ResponseEntity<byte[]>(bytes, headersToForward, responseHttpStatus);
	}

	/**
	 *
	 * @param originalResponse
	 * @return
	 */
	private static HttpHeaders getHeadersToForward(HttpResponse originalResponse) {

		HttpHeaders responseHeaders = new HttpHeaders();

		if (originalResponse == null) {
			return responseHeaders;
		}

		// This is a fallback, we usually will overwrite this with s.th.
		// more specific from the response.
		responseHeaders.setContentType(new MediaType("text", "plain"));

		Header[] originalResponseHeaders = originalResponse.getAllHeaders();

		StringBuffer bufferHeaders = new StringBuffer();

		for (Header header : originalResponseHeaders) {
			String headerName = header.getName();
			String headerVal = header.getValue();

			if (isUnsupportedHeader(headerName)) {
				LOGGER.debug("Unsupported header '" + headerName + "' found "
						+ " and ignored");
			} else {
				headerVal = fixUpHeaderValue(headerName, headerVal);

				// now set the header in the return headers
				responseHeaders.set(headerName, headerVal);
				bufferHeaders.append(headerName + "=" + headerVal + ", ");
			}

		}

		if (responseHeaders.size() > 1) {
			LOGGER.debug("List of headers for the final response of this request: "
					+ bufferHeaders.toString().replaceAll("(,\\s*)$", ""));
		} else {
			LOGGER.debug("No specific headers to forward, "
					+ "setting 'ContentType: text/plain' as fallback");
		}
		return responseHeaders;
	}

	/**
	 *
	 * @param headerName
	 * @return
	 */
	private static boolean isUnsupportedHeader(String headerName) {
		return !isSupportedHeader(headerName);
	}

	/**
	 *
	 * @param headerName
	 * @return
	 */
	private static boolean isSupportedHeader(String headerName) {
		if (headerName == null) {
			return false;
		}
		if (LC_UNSUPPORTED_HEADERS.contains(headerName.toLowerCase())) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param headerName
	 * @param headerVal
	 * @return
	 */
	private static String fixUpHeaderValue(String headerName, String headerVal) {
		if (headerName == null || headerVal == null) {
			return null;
		}

		String logPrefix = "Header '" + headerName + "' has a value '"
				+ headerVal + "'" + " which seems incorrect. ";

		String fixedHeaderVal = headerVal;

		String lowercasecHeaderVal = headerVal.toLowerCase().trim();

		if (lowercasecHeaderVal.contains("subtype")) {
			LOGGER.debug(logPrefix + " Quoting subtype to fix it.");
			fixedHeaderVal = headerVal.replace(UNQUOTED_SUBTYPE_GML,
					QUOTED_SUBTYPE_GML);
		} else if (CONTENT_TYPE_JSON_HINTS.contains(lowercasecHeaderVal)) {
			LOGGER.debug(logPrefix + " Using value '"
					+ JSON_CONTENT_TYPE_HEADER + "'.");
			fixedHeaderVal = JSON_CONTENT_TYPE_HEADER;
		} else if (CONTENT_TYPE_CSV_HINTS.contains(lowercasecHeaderVal)) {
			LOGGER.debug(logPrefix + " Using value '"
					+ JSON_CONTENT_TYPE_HEADER + "'.");
			fixedHeaderVal = CSV_CONTENT_TYPE_HEADER;
		}

		return fixedHeaderVal;
	}

	/**
	 * Extract path from a controller mapping. /controllerUrl/** => return
	 * matched **
	 *
	 * Credits go to: http://stackoverflow.com/a/13937448
	 *
	 * @param request
	 *            incoming request.
	 * @return extracted path
	 */
	private static String extractPathFromPattern(final HttpServletRequest request, String serviceName) {

		String requestUri = request.getRequestURI();

		LOGGER.debug("Request URI is: \"" + requestUri + "\"");

		String contextPath = request.getServletContext().getContextPath();

		String finalPath = requestUri.replace(contextPath + "/" + serviceName, "");

		LOGGER.debug("Final request subpath is: \"" + finalPath + "\"");

		return finalPath;

	}

}
