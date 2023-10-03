import React from "react";

function InsertInfo() {
  return (
    <div style={{ padding: "1rem" }}>
      <div >
        <h2>Choose a cool title </h2>
        <p>
          The only mandatory info is the title, but you can add a description
          and even select between some predefined categories, which will help
          your viewers to find your video. Remember to keep it concise and
          clear, you can add details in further steps.
        </p>
      </div>
    </div>
  );
}

function InsertThumbnail() {
  return (
    <div style={{ padding: "1rem" }}>
      <div>
        <h2>Add an eye-catching thumbnail</h2>
        <p>
          Thumbnails are important as they are the first thing a viewers sees
          about your tutorial, so be sure to choose a nice and precise image to
          describe what you are going to talk about in your video
        </p>
      </div>
    </div>
  );
}

function UploadVideo() {
  return (
    <div style={{ padding: "1rem" }}>
      <div>
        <h2>Upload your video</h2>
        <p>Accepted extensions are .mpg, .mov and .wmv</p>
        <p>
          This will be the video from which your viewers will be able to learn
          from.
          <br></br> Make sure to upload a high quality video with a clear and
          concise concept.
        </p>
      </div>
    </div>
  );
}

export { InsertInfo, InsertThumbnail, UploadVideo };
