package com.pragmatists.eventsourcing.memory;


import com.pragmatists.eventsourcing.api.AggregateId;
import com.pragmatists.eventsourcing.api.Event;
import com.pragmatists.eventsourcing.api.EventStore;
import com.pragmatists.eventsourcing.api.EventStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class InMemoryEventStore implements EventStore {

    private Map<AggregateId, EventStream> events = new HashMap<>();

    public EventStream loadEventStream(AggregateId aggregateId) {
        return events.get(aggregateId);
    }

    @Override
    public EventStream loadEventStream(Predicate<Event> predicate) {
        return new InMemoryEventStream(
                events.values().stream()
                        .flatMap(es -> StreamSupport.stream(es.spliterator(), false))
                        .filter(predicate)
        );
    }

    public void store(AggregateId aggregateId, long version, List<Event> changes) {
        events.computeIfAbsent(aggregateId, id -> InMemoryEventStream.empty()).addAll(changes);
    }


}
