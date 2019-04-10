package com.rpc.framework;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
    private AsynchronousServerSocketChannel server;
    private AsynchronousSocketChannel client;
    private boolean isReadMode;
    private ByteBuffer buffer;

    public void setServer(AsynchronousServerSocketChannel server) {
        this.server = server;
    }

    public AsynchronousServerSocketChannel getServer() {
        return this.server;
    }

    public void setClient(AsynchronousSocketChannel client) {
        this.client = client;
    }

    public AsynchronousSocketChannel getClient() {
        return this.client;
    }

    public void setReadMode(boolean readMode) {
        isReadMode = readMode;
    }

    public boolean isReadMode() {
        return isReadMode;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }
}