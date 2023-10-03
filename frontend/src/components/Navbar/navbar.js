import React, { useState, useContext, useEffect } from "react";
import {
  Avatar,
  Drawer,
  List,
  ListItemText,
  ListItemIcon,
  ListItem,
  Divider,
} from "@mui/material";
import Typography from "@mui/material/Typography";
import { Link, useHistory, useLocation } from "react-router-dom";
import { GlobalContext } from "../../context/globalContext";
import { CgProfile } from "react-icons/cg";
import { AiOutlineUserAdd, AiOutlineUser } from "react-icons/ai";
import { useMediaQuery } from "react-responsive";
import DialogProfileMobile from "./profileMobileDialog";
import { FiVideo, FiSettings } from "react-icons/fi";
import { MdOutlineSubscriptions, MdLogout, MdFeedback } from "react-icons/md";
import { logo } from "../utils/constants";

export default function Navbar() {
  const isTabletOrMobile = useMediaQuery({ query: "(max-width: 500px)" });

  const history = useHistory();
  const location = useLocation();

  const [openDrawer, setOpenDrawer] = useState(false);
  const [show, setShow] = useState(false);
  const [accountPage, setAccountPage] = useState(false);

  const context = useContext(GlobalContext);
  const { loggedUser, setLoggedUser } = context;

  useEffect(() => {
    if (
      location.pathname.includes("/account") ||
      location.pathname.includes("/terms-and-conditions") ||
      location.pathname.includes("/privacy-policy")
    )
      setAccountPage(true);
    else setAccountPage(false);
  }, [location.pathname]);

  const logout = () => {
    setLoggedUser(null);
    localStorage.clear();
    setShow(false);
    setOpenDrawer(false);
    history.push("/");
  };

  const showOptions = () => {
    setShow(true);
  };

  const handleClose = () => {
    setShow(false);
  };

  const toggleDrawer = (open) => (event) => {
    if (
      event.type === "keydown" &&
      (event.key === "Tab" || event.key === "Shift")
    ) {
      return;
    }
    setOpenDrawer(open);
  };

  const avatarAction = () => {
    if (loggedUser) {
      setOpenDrawer(true);
    } else {
      history.push("/account/login");
    }
  };

  const drawer = (
    <div className="drawer-container">
      <List>
        {loggedUser ? (
          <>
            <ListItem style={{ padding: "1rem 0 1rem 2rem" }}>
              <div className="drawer-profile">
                <Avatar
                  src={loggedUser.image}
                  alt="avatar"
                  style={{ width: "54px", height: "54px" }}
                />
                <div style={{ display: "flex", alignItems: "center" }}>
                  <Typography variant="subtitle1">
                    {loggedUser.username}
                  </Typography>
                </div>
              </div>
            </ListItem>
            <Divider />
            <Link
              to="/profile"
              style={{ textDecoration: "none", color: "black" }}
              onClick={() => {
                setOpenDrawer(false);
              }}
            >
              <ListItem button className="list-item">
                <ListItemIcon style={{ minWidth: "30px" }}>
                  <CgProfile className="navbar-icons" />
                </ListItemIcon>
                <ListItemText primary="Profile" />
              </ListItem>
            </Link>
            <Divider />
            <Link
              to="/videos"
              style={{ textDecoration: "none", color: "black" }}
              onClick={() => {
                setOpenDrawer(false);
              }}
            >
              <ListItem button className="list-item">
                <ListItemIcon style={{ minWidth: "30px" }}>
                  <FiVideo className="navbar-icons" />
                </ListItemIcon>
                <ListItemText primary="Videos" />
              </ListItem>
            </Link>
            <Divider />
            <Link
              to="/subscriptions"
              style={{ textDecoration: "none", color: "black" }}
              onClick={() => {
                setOpenDrawer(false);
              }}
            >
              <ListItem button className="list-item">
                <ListItemIcon style={{ minWidth: "30px" }}>
                  <MdOutlineSubscriptions className="navbar-icons" />
                </ListItemIcon>
                <ListItemText primary="Subscriptions" />
              </ListItem>
            </Link>
            <Divider />
            <ListItem button className="list-item" onClick={logout}>
              <ListItemIcon style={{ minWidth: "30px" }}>
                <MdLogout className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Logout" />
            </ListItem>
            <Divider />
          </>
        ) : (
          <>
            <ListItem button className="list-item">
              <Link to="/account/login">
                <ListItemIcon style={{ minWidth: "30px" }}>
                  <AiOutlineUser className="navbar-icons" />
                </ListItemIcon>
                <ListItemText primary="Login" />
              </Link>
            </ListItem>
            <Divider />
            <ListItem button className="list-item">
              <Link to="/account/signup" className="hidelink">
                <ListItemIcon style={{ minWidth: "30px" }}>
                  <AiOutlineUserAdd className="navbar-icons" />
                </ListItemIcon>
                <ListItemText primary="Sign Up" />
              </Link>
            </ListItem>
            <Divider />
          </>
        )}
      </List>
      <div style={{ position: "absolute", bottom: "0", width: "100%" }}>
        <List>
          <Divider />
          <Link
            to="/settings"
            style={{ textDecoration: "none", color: "black" }}
            onClick={() => {
              setOpenDrawer(false);
            }}
          >
            <ListItem button className="list-item">
              <ListItemIcon style={{ minWidth: "30px" }}>
                <FiSettings className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Settings" />
            </ListItem>
          </Link>
          <Divider />
          <Link
            to="/feedback"
            style={{ textDecoration: "none", color: "black" }}
            onClick={() => {
              setOpenDrawer(false);
            }}
          >
            <ListItem button className="list-item">
              <ListItemIcon style={{ minWidth: "30px" }}>
                <MdFeedback className="navbar-icons" />
              </ListItemIcon>
              <ListItemText primary="Guide and feedback" />
            </ListItem>
          </Link>
        </List>
      </div>
    </div>
  );

  return (
    <nav style={{ display: accountPage ? "none" : "block" }}>
      {isTabletOrMobile ? (
        <>
          <div className="nav-container-mobile" style={{ padding: "0.5rem" }}>
            <div>
              <Link to="/" className="hidelink">
                <img src={logo} alt="logo" style={{ width: "10vw" }} />
              </Link>
            </div>
            <div>
              <Typography variant="subtitle1">
                <Link to="/feed" className="navbar-link">
                  Learn
                </Link>
              </Typography>
              <Typography variant="subtitle1">
                <Link to="/teach" className="navbar-link">
                  Teach
                </Link>
              </Typography>
            </div>
            <div style={{ flexGrow: "1" }} />
            <div className="center-and-align">
              {loggedUser ? (
                <Avatar
                  src={loggedUser.image}
                  alt="avatar"
                  className="nav-container-mobile-avatar"
                  onClick={() => showOptions()}
                />
              ) : (
                // <button className="nav-signup-btn">
                  <Link to="/account/login" className="hidelink">
                    <Typography variant="subtitle1" className="navbar-link">Sign in</Typography>
                  </Link>
                // </button>
              )}
            </div>
          </div>
        </>
      ) : (
        <>
          <div>
            <div
              className="nav-container"
              style={{
                height: window.innerHeight > 600 ? "8vh" : "10vh",
              }}
            >
              <div>
                <Link to="/" className="hidelink">
                  <img src={logo} alt="logo" />
                </Link>
                <Typography variant="subtitle1">
                  <Link
                    to="/feed"
                    className="hvr-underline-from-center navbar-link nav-option"
                  >
                    {" "}
                    Learn{" "}
                  </Link>
                </Typography>
                <Typography variant="subtitle1">
                  <Link
                    to="/teach"
                    className="hvr-underline-from-center navbar-link nav-option"
                  >
                    {" "}
                    Teach
                  </Link>
                </Typography>
              </div>
              <div style={{ flexGrow: "1" }} />
              <div>
                {loggedUser ? (
                  <>
                    <Typography
                      variant="subtitle1"
                      className="navbar-link"
                      onClick={() => avatarAction()}
                    >
                      Welcome, {loggedUser?.username}
                    </Typography>
                    <Avatar
                      src={loggedUser?.image}
                      alt="avatar"
                      onClick={avatarAction}
                    />
                  </>
                ) : (
                  <Typography
                    variant="subtitle1"
                    className="navbar-link"
                    onClick={avatarAction}
                  >
                    Sign in
                  </Typography>
                )}
              </div>
            </div>
            <Drawer
              anchor="right"
              open={openDrawer}
              onClose={toggleDrawer(false)}
            >
              {drawer}
            </Drawer>
          </div>
        </>
      )}

      <DialogProfileMobile
        loggedUser={loggedUser}
        handleClose={handleClose}
        show={show}
        logout={logout}
      />
    </nav>
  );
}
