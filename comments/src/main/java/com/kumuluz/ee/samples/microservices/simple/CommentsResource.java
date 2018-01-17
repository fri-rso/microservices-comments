package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.samples.microservices.simple.models.Comments;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/comments")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class CommentsResource {

    //private static final Logger LOG = LogManager.getLogger(CommentsResource.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Vrne seznam vseh komentarjev
     * */
    @Inject
    @Metric(name = "histogram_dodanih")
    Histogram histogram;

    @GET
    @Metered(name = "getComments_meter")
    public Response getComments() {
        //LOG.trace("getComments ENTRY");
        TypedQuery<Comments> query = em.createNamedQuery("Comments.findAll", Comments.class);

        List<Comments> comments = query.getResultList();
        histogram.update(comments.size());
        //LOG.info("Stevilo prikazanih komentarjev: {}", comments.size());
        return Response.ok(comments).build();
    }

    /**
     * Pridobi posamezen komentar glede na njegov id
     */

    @GET
    @Path("/{id}")
    @Timed(name = "getComment_timer")
    public Response getComment(@PathParam("id") Integer id) {
        //LOG.trace("getComment ENTRY");
        Comments p = em.find(Comments.class, id);

        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        //LOG.info("Uspesno prikazan komentart ID: {}", p.getId());
        return Response.ok(p).build();
    }

    /**
     * Omogoča urejanje Komentarja tako, da staremu komenatrju nastavi nove vrednosti
     */
    @POST
    @Path("/{id}")
    public Response editComment(@PathParam("id") Integer id, Comments comments) {
        //LOG.trace("editComment ENTRY");
        Comments p = em.find(Comments.class, id);

        if (p == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        p.setAuthor(comments.getAuthor());
        p.setComment(comments.getComment());
        p.setDate(comments.getDate());
        p.setServiceReference(comments.getServiceReference());

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
        //LOG.info("Uspesno urejen komentart ID: {}", p.getId());
        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Doda nov Komentar (Comments p)
     */
    @POST
    public Response createComment(Comments p) {
        //LOG.trace("createComment ENTRY");
        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
        //LOG.info("Uspesno dodan komentart ID: {}", p.getId());
        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Vrne config info
     * */

    @Inject
    private CommentsProperties properties;

    @GET
    @Path("/config")
    public Response test() {
        //LOG.trace("config ENTRY");
        String response =
                "{" +
                        "\"jndi-name\": \"%s\"," +
                        "\"connection-url\": \"%s\"," +
                        "\"username\": \"%s\"," +
                        "\"password\": \"%s\"," +
                        "\"max-pool-size\": %d" +
                        "}";

        response = String.format(
                response,
                properties.getJndiName(),
                properties.getConnectionUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getMaxPoolSize()
        );
        //LOG.trace("config uspesen EXIT");
        return Response.ok(response).build();
    }
}
