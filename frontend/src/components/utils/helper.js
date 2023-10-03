import moment from "moment";

function timeConverter(videoDuration) {
  var seconds =
    moment.duration(videoDuration)._data.seconds < 10
      ? "0" + moment.duration(videoDuration)._data.seconds
      : moment.duration(videoDuration)._data.seconds;

  var minutes =
    moment.duration(videoDuration)._data.minutes < 10
      ? "0" + moment.duration(videoDuration)._data.minutes
      : moment.duration(videoDuration)._data.minutes;

  var hours =
    moment.duration(videoDuration)._data.hours < 10
      ? "0" + moment.duration(videoDuration)._data.hours
      : moment.duration(videoDuration)._data.hours;

  return hours === "00"
    ? `${minutes}:${seconds}`
    : `${hours}:${minutes}:${seconds}`;
}

function formatTime(video) {
  if (video) {
    var seconds =
      moment.duration(video?.duration)._data.seconds < 10
        ? "0" + moment.duration(video?.duration)._data.seconds
        : moment.duration(video?.duration)._data.seconds;

    var minutes =
      moment.duration(video?.duration)._data.minutes < 10
        ? "0" + moment.duration(video?.duration)._data.minutes
        : moment.duration(video?.duration)._data.minutes;

    var hours =
      moment.duration(video?.duration)._data.hours < 10
        ? "0" + moment.duration(video?.duration)._data.hours
        : moment.duration(video?.duration)._data.hours;

    return `${hours}:${minutes}:${seconds}`;
  }
}

function timeToMilliseconds(hrs, min, sec) {
  var hours = parseInt(hrs);
  var minutes = parseInt(min);
  var seconds = parseInt(sec);

  console.log("hours", hours);
  console.log("minutes", minutes);
  console.log("sec", seconds);
  console.log("total", (hours * 60 * 60 + minutes * 60 + seconds) * 1000);
  return (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
}

export { timeConverter, formatTime, timeToMilliseconds };
