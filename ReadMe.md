# HTTP Server in Java from scratch
Creating a simple HTTP server in Java can be a great way to understand the basics of how servers work, including handling client requests, parsing HTTP headers, and sending responses. This article will guide you step-by-step through building an HTTP server that:
1.	Reads configuration files for settings like the port.
2.	Opens a socket to listen for incoming requests.
3.	Parses HTTP request headers and messages.
4.	Reads files from the file system based on the request.
5.	Sends HTTP response messages back to the client.
# Image 
[![](https://github.com/ATLAS-B28/Jakarta-Http/blob/main/Web_site_success.png)]

## HTTP-Message 
= start-line
  *(header-field CRLF)
  CRLF
  [ message-body ]

The recipient must parse an HTTP message as a
sequence of octets in an encoding that is a 
superset of ASCII.

start-line = request-line / status-line

The request line consists of - method_request-target_HTTP-version CRLF
method - is case-sensitive as it is a token.
method type - GET, POST, HEAD, PUT, DELETE, CONNECT, OPTIONS, TRACE.
request-target - is the target on which the request is to be applied.

## Parsers - 
Types - 
1) Lexer Parser - Stream -> Token -> Method+URI+Version
2) Lexer less Parser - Stream -> Method+URI+Version

HTTP-Message - It is indicated by HTTP-Version field in the 1st line of 
message. HTTP-Version is case-sensitive. 
HTTP-Version = HTTP-Name "/" DIGIT ":" DIGIT
HTTP-Name = %x48.54.54.50 : "HTTP", case-sensitive
The first 2 decimal numbers indicate major and minor version.
Major's minor is indicated and in minor version back-ward compatibility is
shown as subset of protocol, lets the recipient use more advanced features 
can be used in response or in future requests.

## Header Field

1) header-field = field-name ":" OWS field-value OWS
2) field-name = token(this token labels the corresponding field-value as 
                      having the semantics defined by header field)
3) field-value = * (field-content / obs-fold)
3) field-content = field-vchar [ 1*(SP/HTAB) field-varchar]
4) field-vchar = VCHAR/ obs-text
5) obs-text = CRLF 1* (SP/HTAB)
              ; obsolete line folding
              ; 

   
Token = 1 * tchar


tchar = "!" / "#" / "$" / "%" / "&" / "'" / "*"
        / "+" / "-" / "." / "^" / "_" / "|" / "~"
        / DIGIT / ALPHA
        ; any VCHAR, except delimiters



----------------------------------
 #### OG YouTube PlayList - https://www.youtube.com/playlist?list=PLAuGQNR28pW56GigraPdiI0oKwcs8gglW
-----------------------------------
