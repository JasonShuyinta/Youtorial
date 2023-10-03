import * as React from "react";
import {
  Dialog,
  Divider,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  Slide,
  List,
  ListItem,
  ListItemText,
  Avatar,
  ListItemIcon,
  Box,
} from "@mui/material";
import { Link } from "react-router-dom";
import CloseIcon from "@mui/icons-material/Close";
import { CgProfile } from "react-icons/cg";
import { FiVideo, FiSettings } from "react-icons/fi";
import { MdOutlineSubscriptions, MdLogout, MdFeedback } from "react-icons/md";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

export default function DialogProfileMobile({
  loggedUser,
  handleClose,
  show,
  logout,
}) {

  const handleLogout = () => {
    handleClose();
    logout();
  }

  return (
    <Dialog
      fullScreen
      open={show}
      onClose={handleClose}
      TransitionComponent={Transition}
    >
      <AppBar className="edit-appbar">
        <Toolbar>
          <IconButton
            edge="start"
            color="inherit"
            onClick={handleClose}
            aria-label="close"
          >
            <CloseIcon />
          </IconButton>
          <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
            Menu
          </Typography>
        </Toolbar>
      </AppBar>
      <Box className="full-width-height">
        <div className="profile-mobile-avatar-container">
          <Avatar
            src={loggedUser?.image}
            alt="user-avatar"
            style={{ marginRight: "1rem" }}
          />
          <Typography variant="body1">{loggedUser?.username}</Typography>
        </div>
        <Divider />
        <List>
          <Link
            to="/profile"
            className="hidelink"
            onClick={() => handleClose()}
          >
            <ListItem button>
              <ListItemIcon style={{ minWidth: "20px", marginRight: "1rem" }}>
                <CgProfile className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Profile" />
            </ListItem>
          </Link>
          <Divider />
          <Link
            to="/videos"
            className="hidelink"
            onClick={() => handleClose()}
          >
            <ListItem button>
              <ListItemIcon style={{ minWidth: "20px", marginRight: "1rem" }}>
                <FiVideo
                  style={{ color: "black", width: "20px", height: "20px" }}
                />
              </ListItemIcon>
              <ListItemText primary="Videos" />
            </ListItem>
          </Link>
          <Divider />
          <Link
            to="subscriptions"
            className="hidelink"
            onClick={() => handleClose()}
          >
            <ListItem button>
              <ListItemIcon style={{ minWidth: "20px", marginRight: "1rem" }}>
                <MdOutlineSubscriptions className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Subscriptions" />
            </ListItem>
          </Link>
          <Divider />
          <ListItem
            button
            onClick={() => {
              handleLogout();
            }}
          >
            <ListItemIcon style={{ minWidth: "20px", marginRight: "1rem" }}>
              <MdLogout className="navbar-icons" />
            </ListItemIcon>
            <ListItemText primary="Logout" />
          </ListItem>
          <Divider />
        </List>
        <div style={{ position: "absolute", bottom: "0", width: "100%" }}>
          <List>
            <Divider />
            <ListItem button>
              <ListItemIcon>
                <FiSettings className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Settings" />
            </ListItem>
            <Divider />
            <ListItem button onClick={logout}>
              <ListItemIcon>
                <MdFeedback className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Guide and feedback" />
            </ListItem>
          </List>
        </div>
      </Box>
    </Dialog>
  );
}
