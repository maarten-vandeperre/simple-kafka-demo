package com.redhat.demo

import io.smallrye.mutiny.Multi
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.util.concurrent.atomic.AtomicInteger


@Path("kafka-demo")
class KafkaDemoResource {

    private val counter = AtomicInteger(0)

    @Channel("sample-requests-out")
    private lateinit var requestEmitter: Emitter<String>

    @Channel("sample-requests-in")
    private lateinit var requests: Multi<String>

    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    fun createKafkaMessage(): String {
        val message = "message ${counter.incrementAndGet()}"
        requestEmitter.send(message)
        return "Created message '$message'"
    }

    @GET
    @Path("/listen")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    fun stream(): Multi<String> {
        return requests
    }
}