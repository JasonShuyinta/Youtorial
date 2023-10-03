import React, { useState, useEffect, useContext } from "react";
import { FormControlLabel, Checkbox, Typography } from "@mui/material";
import axios from "axios";
import { GlobalContext } from "../../../../context/globalContext";

const BASE_URL = `${process.env.REACT_APP_SERVER_URL}/api/v1`;

export default function CheckStep({ title, stepId, preview }) {
  const [checked, setChecked] = useState(false);

  const context = useContext(GlobalContext);
  const { config, loggedUser, video } = context;

  useEffect(() => {
    let isMounted = true;
    if (!preview && loggedUser && video !== "" && video !== undefined) {
      axios
        .get(
          `${BASE_URL}/interaction/getCheckedStep/${video.id}/${stepId}`,
          config
        )
        .then((res) => {
          if (isMounted) setChecked(res.data);
        })
        .catch((err) => console.log(err));
    }
    return () => {
      isMounted = false;
    };
    //eslint-disable-next-line
  }, [config, preview, stepId, video])

  const handleCheckStep = () => {
    if (!preview && loggedUser && video !== "" && video !== undefined) {
      setChecked(!checked);
      axios
        .get(
          `${BASE_URL}/interaction/checkStep/${video.id}/${stepId}/${!checked}`,
          config
        )
        .then((res) => {
          if (res.status !== 200) {
            console.log(res.status);
          }
        })
        .catch((err) => console.log(err));
    } else if (!loggedUser) alert("You need to login");
  };

  return (
    <>
      {preview ? (
        <Typography variant="body1" className="checkstep-title">
          {title}
        </Typography>
      ) : (
        <FormControlLabel
          label={title}
          onClick={(event) => event.stopPropagation()}
          onFocus={(event) => event.stopPropagation()}
          control={
            <Checkbox
              checked={checked}
              onClick={handleCheckStep}
              style={{ color: "#181818" }}
            />
          }
        />
      )}
    </>
  );
}
