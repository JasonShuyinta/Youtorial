import React, { useState, useEffect } from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import Comment from "./comment";

export default function ReplyComment({
  commentsList,
  parentComment,
  loggedUser,
  handleRefresh,
}) {
  const [openReplyComments, setOpenReplyComments] = useState(false);
  const [replyNumber, setReplyNumber] = useState(0);

  useEffect(() => {
    let commentNumber = 0;
    commentsList.forEach((comment) => {
      if (comment.responseTo === parentComment) commentNumber++;
    });
    setReplyNumber(commentNumber);
  }, [commentsList, parentComment]);

  let renderReplyComment = (parentComment) =>
    commentsList.map((comment, index) => (
      <div key={comment.id}>
        {comment.responseTo === parentComment && (
          <div style={{ marginLeft: "55px" }}>
            <Comment
              comment={comment}
              loggedUser={loggedUser}
              handleRefresh={handleRefresh}
            />
            <ReplyComment
              commentsList={commentsList}
              parentComment={comment._id}
              loggedUser={loggedUser}
              handleRefresh={handleRefresh}
            />
          </div>
        )}
      </div>
    ));

  return (
    <>
      <div style={{ display: replyNumber === 0 ? "none" : "block" }}>
        <div style={{ display: "flex" }}>
          <p
            onClick={() => setOpenReplyComments(!openReplyComments)}
            className="replyCommentStyle"
          >
            {openReplyComments ? "Hide" : "View"} {replyNumber} comment(s)
          </p>
          {openReplyComments ? (
            <ExpandLessIcon style={{ color: "gray" }} />
          ) : (
            <ExpandMoreIcon style={{ color: "gray" }} />
          )}
        </div>
      </div>
      {openReplyComments && renderReplyComment(parentComment)}
    </>
  );
}
