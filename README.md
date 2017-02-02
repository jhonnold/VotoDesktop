# voto-desktop

Currently the method for discovery and connecting a server to client:

Servers are running, connected to the Multicast IP: 224.0.0.3 (Send a datagram packet to this IP to get a response)

Upon receiving the handshake request, "VOTO_HANDSHAKE_REQUEST", the server will reply VOTO_HANDSHAKE_RESPONSE_[id]

The client can then choose which to link to and reply again with VOTO_HANDSHAKE_CONNECT_[passcode]

If it checks out, the client will be added to the voting poll for the server.
