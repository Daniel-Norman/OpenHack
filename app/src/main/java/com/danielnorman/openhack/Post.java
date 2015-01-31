package com.danielnorman.openhack;

/**
 * A class to contain and return data about a post.
 * Has a path to an image and a caption currently
 * Will have a location and possibly likes.
 */
public class Post {
    private String pathToImage;
    private String caption;

    public Post(String path, String caption)
    {
        pathToImage = path;
        this.caption = caption;
    }

    public String getPathToImage()
    {
        return pathToImage;
    }

    public String getCaption()
    {
        return caption;
    }
}
