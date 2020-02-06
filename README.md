# JAVA based Webserver for Code Challenge

This project is for a Code Challenge to develop a simple Webserver with the following requirements:
The webserver should run on port 23456.
❏ The webserver has an endpoint at /api/encode.
❏ This endpoint should accept post requests.
❏ This endpoint expects a JSON string to be received. The JSON structure should
have two keys.
❏ The JSON structure should have a key Shift. It’s value should be an
integer.
❏ The JSON structure should have a key Message. It’s value should be a
string.

❏ When the JSON payload is received, it should encode the provided message
using the shift cipher method. If multiple words are provided, spaces should be
retained.
- The shift cipher works by shifting each letter a given number of letters to
the right in alphabet order. The letter A shifted 1 would be B. B shifted 2
would be D.
- If Shift = 3 and Message = “dad”, the encoded message would be
generated as “gdg”.

- If Shift = 2 and Message = “the eagle has landed”, the encoded message
would be generated as “vjg gcing jcu ncpfgf”.
- This should work for any positive shift value provided. If Shift = 522 and
Message = “the eagle has landed”, the encoded message would be
generated as “vjg gcing jcu ncpfgf”.

❏ When the message is successfully encoded, it should be stored in a file on disk.
❏ The endpoint should return a 200 if successful, 500 if unsuccessful.
❏ The endpoint should return a json object with a single key EncodedMessage.
The value returned should be the string you successfully encoded. If
unsuccessful, it should return an empty string.

### Prerequisites
Git
Maven 3.6.3+
Java8

### Installing
1. Clone the repository local
2. Switch to the project directory
3. Issue 'mvn clean package' to build the jar file
4. Change directories to the <project>/target/ directory
5. Launch the server with 'java -jar XXXXXX'
