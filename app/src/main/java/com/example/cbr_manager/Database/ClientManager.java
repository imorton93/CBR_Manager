package com.example.cbr_manager.Database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ClientManager implements Iterable<Client>{

    private static ClientManager instance;
    private List<Client> clients = new ArrayList<>();

    public ClientManager() {

    }

    public static ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }

        return instance;
    }

    @NonNull
    @Override
    public Iterator<Client> iterator() {
        return clients.iterator();
    }

    public List<Client> getClientsAsLists() {
        return clients;
    }

    public Client getClientAtIndex(int position) {
        return clients.get(position);
    }
}
