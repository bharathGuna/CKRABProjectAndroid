package com.finalproject.cs4962.whale;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Networking
{
    public class CreateAccountResponse
    {
        public String userID;
    }

    public static final String SERVER_IP = "192.168.1.22";
    public static final int SERVER_PORT = 2000;

    public static CreateAccountResponse createNewAccount(String username)
    {
        try
        {
            Socket connection = new Socket(SERVER_IP, SERVER_PORT);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "newacct";
            String message = String.format("{ \"username\" : \"%s\" }", username);
            byte[] payload = packMessage(type, message);


            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            short inLen = toShort(byte1, byte2);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = new String(response, "ASCII");
            Gson gson = new Gson();

            CreateAccountResponse createAccountResponse = gson.fromJson(json, CreateAccountResponse.class);
            return createAccountResponse;
        }
        catch (Exception e)
        {
            Log.i("OOPS", "Something went wrong: " + e);
            return null;
        }
    }

    private static byte[] packMessage(String mt, String msg)
    {
        byte[] type = stringToByteArray(mt);
        byte[] payload = stringToByteArray(msg);
        short outLen = (short) payload.length;
        byte[] length = fromShort(outLen);

        List<Byte> out = new ArrayList<>();

        for(int i = 0; i < type.length; i++)
            out.add(type[i]);
        for (int i = 0; i < 2; i++)
            out.add(length[i]);
        for(int i = 0; i < payload.length; i++)
            out.add(payload[i]);

        byte[] send = toPrimitives(out.toArray(new Byte[out.size()]));
        return send;
    }

    private static byte[] stringToByteArray(String payload)
    {
        try
        {
            return payload.getBytes("ASCII");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static byte[] toPrimitives(Byte[] oBytes)
    {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++)
            bytes[i] = oBytes[i];
        return bytes;
    }

    private static byte[] fromShort(short num)
    {
        byte byte2 = (byte) (num >> 8);
        byte byte1 = (byte) (num & 0xFF);
        return new byte[] {byte1, byte2};
    }

    private static short toShort(byte byte1, byte byte2)
    {
        return (short) ((byte2 << 8) + byte1);
    }
}
