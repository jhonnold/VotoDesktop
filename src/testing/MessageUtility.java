package testing;

import java.nio.ByteBuffer;

/**
 * Created by nicholasyamahamanschweikart on 3/1/17.
 * Test class
 */

public class MessageUtility {
    private static final String TAG = "message-util";
    public static final byte
            HANDSHAKE_REQUEST   = (byte) 'R',
            VOTE_REQUEST        = (byte) 'V',
            VOTE_RESPONSE       = (byte) 'R',
            MEDIA_REQUEST       = (byte) 'M',
            MEDIA_PING          = (byte) 'P',
            MEDIA_RESPONSE      = (byte) 'R';


    /**
     * Builds a handshake request message.
     * @param id your unique ID if you have one, NULL if not
     * @return the byte[] message
     */
    public static byte[] getHandshakeRequestMessage(String id){
        byte[] message;
        int i = 0;
        if(id != null){
            byte len = (byte) id.length();
            byte[] idBuff = id.getBytes();
            message = new byte[ 1 + 1 + len];
            message[i++] = HANDSHAKE_REQUEST;
            message[i++] = len;
            System.arraycopy(idBuff,0,message,i,len);
        }else{
            message = new byte[ 1 + 1];
            message[i++] = HANDSHAKE_REQUEST;
            message[i] = 0;
        }
        return message;
    }

    public static byte[] getVoteMessage(String id, String vote, byte voteID){

        byte[] message;
        int i = 0;
        byte voteLength = ((byte) vote.length());
        byte[] voteData = vote.getBytes();

        if(id != null){
            byte idLength = (byte) id.length();
            byte[] idBuff = id.getBytes();

            // [ V | ID_LEN | ID_DATA | VOTE_ID | VOTE_LEN | VOTE_DATA]
            message = new byte[ 1 + 1 + idLength + 1 + 1 + voteLength];
            message[i++] = VOTE_REQUEST;
            message[i++] = idLength;

            // Copy in the ID data
            System.arraycopy(idBuff,0,message,i,idLength);
            i += idLength;

            message[i++] = voteID;
            message[i++] = voteLength;

            System.arraycopy(voteData,0,message,i,voteLength);

        }else{
            // [ V | ID_LEN | VOTE_ID | VOTE_LEN | VOTE_DATA]
            message = new byte[ 1 + 1 + 1 + 1 + voteLength];
            message[i++] = VOTE_REQUEST;
            message[i++] = 0;           // No ID field
            message[i++] = voteID;
            message[i++] = voteLength;

            System.arraycopy(voteData,0,message,i,voteLength);
        }

        return message;
    }

    public static byte[] getMediaPingMessage(){
        byte[] message = new byte[1 + 1];
        message[0] = MEDIA_REQUEST;
        message[1] = MEDIA_PING;
        return message;
    }

    public static byte[] getMediaRequestMessage(byte imgID, int packetNumber){
        byte[] message = new byte[3];
        message[0] = MEDIA_REQUEST;
        message[1] = MEDIA_RESPONSE;
        message[2] = imgID;
        
        message = append(message, ByteBuffer.allocate(4).putInt(packetNumber).array());
        
        return message;
    }

    public static byte parseVoteResponse(byte[] message){
        byte voteID = -1;
        if(message[0] == VOTE_REQUEST && message[1] == VOTE_RESPONSE){
            voteID = message[2];
        }
        return voteID;
    }

    public static boolean parseMediaPing(byte[] msg, Client.MediaResponse res){

        // Check for proper message headers
        if(msg[0] == MEDIA_REQUEST && msg[1] == MEDIA_PING ){

            // Extract image ID
            res.imgID = msg[2];

            // Extract packet number
            res.packetCount = ByteBuffer.wrap(msg, 3, 4).getInt();

            res.imgLength = ByteBuffer.wrap(msg, 7, 4).getInt();
            return true;
        }

        return false;
    }

    public static boolean parseMediaResponse(byte[] msg, Media media){

        // Check for proper message headers
        if(msg[0] == MEDIA_REQUEST && msg[1] == MEDIA_RESPONSE ){

            // Extract image ID
            byte imgID = msg[2];

            // Extract packet number
            int packetNumber = ByteBuffer.wrap(msg, 3, 4).getInt();

            if(media.getImgID() == imgID && media.getExpectingPacketNumber() == packetNumber){
                int payloadLength = ByteBuffer.wrap(msg,7,4).getInt();
                byte[] payload = new byte[payloadLength];
                System.arraycopy(msg, 11, payload, 0, payloadLength);
                media.appendData(payload);
            }
            return true;
        }

        return false;
    }
    
	private static byte[] append(byte[] data, byte[] addition) {
		byte[] temp = new byte[data.length + addition.length];
		
		for (int i = 0; i < data.length; i++) {
			temp[i] = data[i];
		}
		
		for (int i = 0; i < addition.length; i++) {
			temp[i + data.length] = addition[i];
		}
		
		return temp;
	}


}
