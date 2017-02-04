# voto-desktop

Currently the method for discovery and connecting a server to client:

Here's how it works so far:
-- Run the Server.java, it'll start by connecting to a mutlicast IP (224.0.0.3) and wait for "VOTO_HANDSHAKE_REQUEST"
       -The Client.java will connect to this and send the expected packet

-- Once it receives this, it will send back a packet and it will start a TCPServer on the same port. 

-- I wrote a quick TCPClient code to test to connect to it, just change the ip in there too w/e the IP of the server is.

