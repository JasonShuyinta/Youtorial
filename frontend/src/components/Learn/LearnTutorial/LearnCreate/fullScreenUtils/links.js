import React, { useState, useContext } from "react";
import { Grid, TextField, Button, Chip } from "@mui/material";
import { CreationContext } from "../../../../../context/creationContext";

export default function Links({ handleAddLink, handleRemoveLink }) {
  const [name, setName] = useState("");
  const [link, setLink] = useState("");

  const create = useContext(CreationContext);
  const { requisites } = create;

  const manageLink = () => {
    if (!name || !link) alert("Fields cannote be empty");
    if (requisites.length > 4) {
      alert("Max 5 links per steps allowed");
      return;
    }
    var obj = { name, link };
    handleAddLink({ obj });
    setName("");
    setLink("");
  };

  return (
    <Grid container spacing={1}>
      <Grid item xs={12} className="linksDiv">
        <TextField
          label="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          variant="outlined"
          inputProps={{ maxLength: 50 }}
          className="linkField"
          style={{ marginRight: "1rem" }}
        />
        <TextField
          label="URL"
          value={link}
          onChange={(e) => setLink(e.target.value)}
          variant="outlined"
          className="linkField"
          style={{ marginRight: "1rem" }}
        />
        <Button
          onClick={manageLink}
          variant="contained"
          style={{
            margin: "0.8rem 1rem",
            backgroundColor: "black",
            color: "white",
          }}
        >
          {" "}
          Add{" "}
        </Button>
      </Grid>
      <Grid item xs={12}>
        {requisites &&
          requisites.length !== 0 &&
          requisites.map((el, index) => {
            return (
              <Chip
                label={el?.name}
                key={index}
                onClick={() => window.open(el?.link)}
                clickable
                variant="outlined"
                onDelete={() => handleRemoveLink(el)}
                style={{
                  margin: "1rem 1rem 1rem 0",
                  display: el.name === "" && "none",
                }}
              />
            );
          })}
      </Grid>
    </Grid>
  );
}
