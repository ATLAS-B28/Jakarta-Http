# HTTP Server in Java from scratch
1) Read Config Files
2) Open a Socket to listen at a Port
3) Read Request Headers and Messages(Body)
4) Open and read the files from the File-system
5) Write and send back the Response Messages

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