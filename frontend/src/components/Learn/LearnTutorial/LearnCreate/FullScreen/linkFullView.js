import React, { useState } from "react";
import { Grid, TextField, Button, Chip } from "@mui/material";

export default function LinkFullView({
  requisites,
  handleAddLink,
  handleRemoveLink,
}) {
  const [name, setName] = useState("");
  const [link, setLink] = useState("");

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
    <Grid
      container
      spacing={3}
      style={{ paddingTop: "0.5rem", marginTop: "1rem" }}
    >
      <Grid item xs={4} style={{ padding: "0" }}>
        <TextField
          label="Name"
          value={name}
          onChange={(e) => {
            setName(e.target.value);
          }}
          variant="outlined"
          inputProps={{ maxLength: 20 }}
          className="linkField"
        />
      </Grid>
      <Grid item xs={4} style={{ padding: "0" }}>
        <TextField
          label="Link"
          value={link}
          onChange={(e) => {
            setLink(e.target.value);
          }}
          variant="outlined"
          className="linkField"
        />
      </Grid>
      <Grid item xs={4} style={{ padding: "0" }}>
        <Button
          onClick={manageLink}
          color="primary"
          variant="contained"
          className="btn-style"
          style={{ marginTop: "0.5rem" }}
        >
          Add
        </Button>
      </Grid>
      <Grid item xs={12} style={{ textAlign: "left", marginTop: "1rem" }}>
        {requisites.length !== 0 &&
          requisites.map((req, index) => {
            return (
              <Chip
                label={req.name}
                key={index}
                onClick={() => window.open(req.link)}
                clickable
                onDelete={() => handleRemoveLink(req)}
                variant="outlined"
                style={{ margin: "0 1rem 1rem 0" }}
              />
            );
          })}
      </Grid>
    </Grid>
  );
}
