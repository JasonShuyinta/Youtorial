import React, { useEffect, useState } from "react";
import axios from "axios";
import Comment from "./Comment/comment";
import ReplyComment from "./Comment/replyComment";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function CommentList({
  videoId,
  refresh,
  loggedUser,
  handleRefresh,
}) {
  const [comments, setComments] = useState([]);

  useEffect(() => {
    if (videoId) {
      axios
        .get(`${BASE_URL}/comment/getComments/${videoId}`)
        .then((res) => {
          if (res.status === 204) {
            setComments([]);
          } else {
            setComments(res.data);
          }
        })
        .catch((err) => console.log(err));
    }
    // eslint-disable-next-line
  }, [refresh, videoId]);

  return (
    <>
      {comments.map(
        (comment) =>
          !comment.responseTo && (
            <React.Fragment key={comment.id}>
              <Comment
                comment={comment}
                loggedUser={loggedUser}
                handleRefresh={handleRefresh}
              />
              <ReplyComment
                commentsList={comments}
                parentComment={comment.id}
                loggedUser={loggedUser}
                handleRefresh={handleRefresh}
              />
            </React.Fragment>
          )
      )}
    </>
  );
}
