package com.dem.ElasticsearchDemo.Config;

import org.elasticsearch.action.ActionListener;

import java.util.concurrent.CompletableFuture;

public class Listener implements ActionListener {

    private final CompletableFuture future;

    public Listener(CompletableFuture future) {
        this.future = future;
    }

    @Override
    public void onResponse(Object o) {
        future.complete(o);
    }

    @Override
    public void onFailure(Exception e) {
        System.out.println("error while processing Request "+e.getMessage());
        future.completeExceptionally(e);
    }
}
