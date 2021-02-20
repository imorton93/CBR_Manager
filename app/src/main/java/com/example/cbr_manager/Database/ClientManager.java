package com.example.cbr_manager.Database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ClientManager implements Iterable<Client>{

    private List<Client> clients = new ArrayList<>();

    public ClientManager() {
    }

    @NonNull
    @Override
    public Iterator<Client> iterator() {
        return clients.iterator();
    }
}
