package ch.heigvd.amt;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class BlogResource {

    @Inject
    BlogService blogService;

    @Inject
    Template index;

    @Blocking
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return index.data("posts", blogService.findAllPosts());
    }

    @Inject
    Template create;

    @Blocking
    @Path("/posts/create.html")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance create() {
        return create.instance();
    }

    @Inject
    Template created;

    @Blocking
    @Path("/posts/")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance save(@FormParam("title") String title, @FormParam("content") String content) {
        var post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setSlug(title.toLowerCase().replace(" ", "-"));
        post.setDate(java.time.Instant.now());
        blogService.createPost(post);
        return created.instance();
    }

    @Inject
    Template view;

    @Blocking
    @Path("/posts/{slug}.html")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance view(@PathParam("slug") String slug) {
        return view.data("post", blogService.findPostBySlug(slug));
    }

}
