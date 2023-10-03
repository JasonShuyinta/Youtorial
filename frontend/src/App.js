import React, { lazy, Suspense } from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Footer from "./components/Footer/footer";
import ScrollToTop from "./components/Home/scrollToTop";
import Navbar from "./components/Navbar/navbar";
import Verify from "./components/Account/verify";
import Verified from "./components/Account/verified";
import { Privacy, Terms } from "./components/utils/privacy-terms";

const Signup = lazy(() => import("./components/Account/signup"))
const Login = lazy(() => import("./components/Account/login"))
const Home = lazy(() => import("./components/Home/home"))
const Feed = lazy(() => import("./components/Feed/feed"))
const Teach = lazy(() => import("./components/Teach/teach"));
const Profile = lazy(() => import("./components/Account/profile"))
const Learn = lazy(() => import("./components/Learn/learn"))
const VideoUploaded = lazy(() => import("./components/Account/videoUploaded"));
const EditVideo = lazy(() => import("./components/Learn/LearnTutorial/editVideo"))
const EditTutorial = lazy(() => import("./components/Learn/LearnTutorial/editTutorial"));
const Channels = lazy(() => import("./components/Account/channels"));
const Settings = lazy(() => import("./components/Account/settings"));
const Feedback = lazy(() => import("./components/utils/feedback"));
const Tutor = lazy(() => import("./components/Account/tutor"));
const PageNotFound = lazy(() => import("./components/utils/pageNotFound"));


function App() {
  return (
    <Suspense fallback={<RenderLoading/>}>
      <BrowserRouter>
        <ScrollToTop/>
        <Navbar />
        <Switch>
          <Route path="/account/signup" component={Signup} />
          <Route path="/account/login" component={Login} />
          <Route path="/account/verify" component={Verify} />
          <Route path="/account/verified/:verificationCode" component={Verified}/>
          <Route exact path="/" component={Home} />
          <Route path="/feed" component={Feed} />
          <Route path="/teach" component={Teach} />
          <Route path="/profile" component={Profile} />
          <Route path="/learn" component={Learn} />
          <Route path="/videos" component={VideoUploaded} />
          <Route path="/video/edit-video/:videoId" component={EditVideo} />
          <Route path="/video/edit-tutorial/:videoId" component={EditTutorial} />
          <Route path="/subscriptions" component={Channels} />
          <Route path="/settings" component={Settings}/>
          <Route path="/feedback" component={Feedback}/>
          <Route path="/tutor/:tutorUsername" component={Tutor} />
          <Route path="/terms-and-conditions" component={Terms}/>
          <Route path="/privacy-policy" component={Privacy}/>
          <Route component={PageNotFound} />
        </Switch>
        <Footer />
      </BrowserRouter>
    </Suspense>
  );
}

const RenderLoading = () => {
  return (
    <div style={{height: "100vh", width: "100%"}}>
      <div className="loader"></div>
    </div>
  )
}

export default App;
