# VOTO-DESKTOP

## Folder Layout
### controller
NetworkController and SessionController. These two classes work in unison to manage packets incoming and outcoming and communicate them with the current session
### networking
UDPSocket is a DatagramSocket wrapped in a class and stuck on an inifite loop. It also sends out packets through this.
### session
Session stuff, very basic implementation for initial sprint
### testing
Server - RUN ME TO TEST THE CODE SO FAR. Everything else is for testing quickly and efficiently.
### utilities
Command Parser just parses the commands from networking controller to session controller.

## Client Information
### Connect to server
Send: handshakeRequest_[id] <-- If no id leave blank, but underscore must remain!
### Sending vote
Send: vote_[id]_[votestring] <-- Again if no id leave blank, but underscore must remain (it will be 2 underscores)

This is currently handled, but not implemented properly

