package com.youtorial.backend.model.Video;

import org.springframework.stereotype.Component;


@Component
public class VideoConverter {

    public Video dtoInputToEntity(VideoDtoInput videoDtoInput) {
        Video video = new Video();
        video.setId(videoDtoInput.getId());
        video.setTitle(videoDtoInput.getTitle());
        video.setDescription(videoDtoInput.getDescription());
        video.setUrl(videoDtoInput.getUrl());
        video.setThumbnail(videoDtoInput.getThumbnail());
        video.setDuration(videoDtoInput.getDuration());
        video.setCategory(videoDtoInput.getCategory());
        video.setLocation(videoDtoInput.getLocation());

        return video;
    }

    public VideoDtoOutput entityToDtoOutput(Video video) {
        VideoDtoOutput videoDtoOutput = new VideoDtoOutput();
        videoDtoOutput.setId(video.getId());
        videoDtoOutput.setTitle(video.getTitle());
        videoDtoOutput.setDescription(video.getDescription());
        videoDtoOutput.setUploadDate(video.getUploadDate().toString());
        videoDtoOutput.setUrl(video.getUrl());
        videoDtoOutput.setThumbnail(video.getThumbnail());
        videoDtoOutput.setDuration(video.getDuration());
        videoDtoOutput.setAuthor(video.getAuthor());
        videoDtoOutput.setCategory(video.getCategory());
        videoDtoOutput.setTutorialId(video.getTutorialId());
        videoDtoOutput.setLocation(video.getLocation());
        return videoDtoOutput;
    }
}
