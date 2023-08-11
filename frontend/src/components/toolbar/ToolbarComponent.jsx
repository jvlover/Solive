import React, {Component} from "react";
import "./ToolbarComponent.css";

import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";

import Mic from "@mui/icons-material/Mic";
import MicOff from "@mui/icons-material/MicOff";
import Videocam from "@mui/icons-material/Videocam";
import VideocamOff from "@mui/icons-material/VideocamOff";
import Fullscreen from "@mui/icons-material/Fullscreen";
import FullscreenExit from "@mui/icons-material/FullscreenExit";
import PictureInPicture from "@mui/icons-material/PictureInPicture";
import ScreenShare from "@mui/icons-material/ScreenShare";
import StopScreenShare from "@mui/icons-material/StopScreenShare";
import Tooltip from "@mui/material/Tooltip";
import PowerSettingsNew from "@mui/icons-material/PowerSettingsNew";
import QuestionAnswer from "@mui/icons-material/QuestionAnswer";

import IconButton from "@mui/material/IconButton";

import logo from "../../assets/logo_white.png";

export default class ToolbarComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {fullscreen: false};
        this.camStatusChanged = this.camStatusChanged.bind(this);
        this.micStatusChanged = this.micStatusChanged.bind(this);
        this.screenShare = this.screenShare.bind(this);
        this.stopScreenShare = this.stopScreenShare.bind(this);
        this.toggleFullscreen = this.toggleFullscreen.bind(this);
        this.leaveSession = this.leaveSession.bind(this);
        this.toggleChat = this.toggleChat.bind(this);
    }

    micStatusChanged() {
        this.props.micStatusChanged();
    }

    camStatusChanged() {
        this.props.camStatusChanged();
    }

    screenShare() {
        this.props.screenShare();
    }

    stopScreenShare() {
        this.props.stopScreenShare();
    }

    toggleFullscreen() {
        this.setState({fullscreen: !this.state.fullscreen});
        this.props.toggleFullscreen();
    }

    leaveSession() {
        this.props.leaveSession();
        // 여기 조건문 들어서 세션에 있을 때만 뜨게 해야함
        if (this.props.sessionId !== null) {
            alert(this.props.sessionId + "에서 나가버렸어요");
        } else {
            alert("이미 세션에서 나간 상태입니다")
            // 아예 다른 창으로 옮겨야 하지 않을까
        }
    }

    toggleChat() {
        this.props.toggleChat();
    }

    render() {
        const sessionId = this.props.sessionId;
        const localUser = this.props.user;
        return (
            <AppBar className="toolbar" id="header">
                <Toolbar className="toolbar">
                    <div id="navSessionInfo">
                        {/* 여기는 로고 */}
                        <img id="header_img" alt="Solive Logo" src={logo}/>
                        {this.props.sessionId && (
                            <div id="titleContent">
                                <span id="session-title">{sessionId}</span>
                            </div>
                            // 여기는 세션 있으면 세션 이름 보여줍니다.
                        )}
                    </div>
                    <div className="buttonsContent">
                        {/*마이크버튼*/}
                        <IconButton
                            // micon일 때 설정들
                            color="inherit"
                            className="navButton"
                            id="navMicButton"
                            onClick={this.micStatusChanged}
                        >
                            {/* 유저 있고 마이크 온이면 mic 아이콘 아니면 micoff 아이콘 */}
                            {localUser !== undefined &&
                            localUser.isAudioActive() ? (
                                <Mic/>
                            ) : (
                                <MicOff color="secondary"/>
                            )}
                        </IconButton>

                        {/*카메라 버튼*/}
                        <IconButton
                            color="inherit"
                            className="navButton"
                            id="navCamButton"
                            onClick={this.camStatusChanged}
                        >
                            {localUser !== undefined &&
                            localUser.isVideoActive() ? (
                                <Videocam/>
                            ) : (
                                <VideocamOff color="secondary"/>
                            )}
                        </IconButton>

                        {/*화면공유버튼*/}
                        <IconButton
                            color="inherit"
                            className="navButton"
                            onClick={this.screenShare}
                        >
                            {localUser !== undefined &&
                            localUser.isScreenShareActive() ? (
                                // 이미 화면 공유 중일 때는 다른 화면으로 바꾸는 버튼
                                <PictureInPicture/>
                            ) : (
                                <ScreenShare/>
                            )}
                        </IconButton>

                        {/*화면공유 그만 버튼*/}
                        {localUser !== undefined &&
                            localUser.isScreenShareActive() && (
                                <IconButton
                                    onClick={this.stopScreenShare}
                                    id="navScreenButton"
                                >
                                    <StopScreenShare color="secondary"/>
                                </IconButton>
                            )}

                        {/*전체화면 버튼*/}
                        <IconButton
                            color="inherit"
                            className="navButton"
                            onClick={this.toggleFullscreen}
                        >
                            {localUser !== undefined &&
                            this.state.fullscreen ? (
                                <FullscreenExit/>
                            ) : (
                                <Fullscreen/>
                            )}
                        </IconButton>

                        {/*세션 떠나기 버튼 */}
                        <IconButton
                            color="secondary"
                            className="navButton"
                            onClick={this.leaveSession}
                            id="navLeaveButton"
                        >
                            <PowerSettingsNew/>
                        </IconButton>
                        {/*채팅창 버튼*/}
                        <IconButton
                            color="inherit"
                            onClick={this.toggleChat}
                            id="navChatButton"
                        >
                            {this.props.showNotification && (
                                <div id="point" className=""/>
                            )}
                            {/*마우스 올리면 뜨는거*/}
                            <Tooltip title="채팅창">
                                <QuestionAnswer/>
                            </Tooltip>
                        </IconButton>
                    </div>
                </Toolbar>
            </AppBar>
        );
    }
}
