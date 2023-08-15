import { useState } from 'react';
import "./MatchToolbar.css";

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
import { Box, Modal } from '@mui/material';
import { Typography } from '@material-tailwind/react';
import Button from '@mui/material/Button';

const MatchToolbar = (props) => {
    const [fullscreen, setFullscreen] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);

    const modalToggle = () => {
        setModalOpen(!modalOpen)
    }

    const micStatusChanged = () => {
        props.micStatusChanged();
    }

    const camStatusChanged = () => {
        props.camStatusChanged();
    }

    const screenShare = () => {
        props.screenShare();
    }

    const stopScreenShare = () => {
        props.stopScreenShare();
    }

    const toggleFullscreen = () => {
        setFullscreen(!fullscreen);
        props.toggleFullscreen();
    }

    const leaveSession = () => {
        props.leaveSession();
        if (props.sessionId !== null) {
            alert("강의방에서 퇴장하셨습니다.");
        } else {
            alert("이미 퇴장하신 상태입니다.")
        }
    }

    const toggleChat =() => {
        props.toggleChat();
    }

    const localUser = props.user;

        return (
            <AppBar className="toolbar" id="header">
                <Toolbar className="toolbar" style={{height:80}}>
                    <div id="navSessionInfo">
                        {/* 여기는 로고 */}
                        <img id="header_img" alt="Solive Logo" src={logo}/>
                    </div>
                    <div className="buttonsContent">
                        {/*마이크버튼*/}
                        <IconButton
                            // micon일 때 설정들
                            color="inherit"
                            size="large"
                            className="navButton"
                            id="navMicButton"
                            onClick={micStatusChanged}
                        >
                            {/* 유저 있고 마이크 온이면 mic 아이콘 아니면 micoff 아이콘 */}
                            {localUser !== undefined &&
                            localUser.isAudioActive() ? (
                                <Tooltip title="음성공유 중지">
                                    <Mic/>
                                </Tooltip>
                            ) : (
                                <Tooltip title="음성공유">
                                    <MicOff color="inherit"/>
                                </Tooltip>
                            )}
                        </IconButton>

                        {/*카메라 버튼*/}
                        <IconButton
                            color="inherit"
                            size="large"
                            className="navButton"
                            id="navCamButton"
                            onClick={camStatusChanged}
                        >
                            {localUser !== undefined &&
                            localUser.isVideoActive() ? (
                                <Tooltip title="화상공유 중지">
                                    <Videocam/>
                                </Tooltip>
                            ) : (
                                <Tooltip title="화상공유">
                                    <VideocamOff color="inherit"/>
                                </Tooltip>
                            )}
                        </IconButton>

                        {/*화면공유버튼*/}
                        <IconButton
                            color="inherit"
                            size="large"
                            className="navButton"
                            onClick={screenShare}
                        >
                            {localUser !== undefined &&
                            localUser.isScreenShareActive() ? (
                                // 이미 화면 공유 중일 때는 다른 화면으로 바꾸는 버튼
                                <Tooltip title="다른화면공유">
                                    <PictureInPicture/>
                                </Tooltip>
                            ) : (
                                <Tooltip title="화면공유">
                                    <ScreenShare/>
                                </Tooltip>
                            )}
                        </IconButton>

                        {/*화면공유 중지 버튼*/}
                        {localUser !== undefined &&
                            localUser.isScreenShareActive() && (
                                <IconButton
                                    color="inherit"
                                    onClick={stopScreenShare}
                                    size="large"
                                    id="navScreenButton"
                                >
                                    <Tooltip title="화면공유 중지">
                                        <StopScreenShare/>
                                    </Tooltip>
                                </IconButton>
                            )}

                        {/*전체화면 버튼*/}
                        <IconButton
                            color="inherit"
                            size="large"
                            className="navButton"
                            onClick={toggleFullscreen}
                        >
                            {localUser !== undefined &&
                            fullscreen ? (
                                <Tooltip title="전체화면 중지">
                                    <FullscreenExit/>
                                </Tooltip>
                            ) : (
                                <Tooltip title="전체화면">
                                    <Fullscreen/>
                                </Tooltip>
                            )}
                        </IconButton>

                        {/*세션 떠나기 버튼 */}
                        <IconButton
                            color="error"
                            size="large"
                            className="navButton"
                            onClick={modalToggle}
                            id="navLeaveButton"
                        >
                            <Modal
                              open={modalOpen}
                              onClose={modalToggle}
                            >
                                <Box sx={{
                                    position: 'absolute',
                                    top: '50%',
                                    left: '50%',
                                    transform: 'translate(-50%, -50%)',
                                    width: 400,
                                    bgcolor: 'background.paper',
                                    border: '2px solid #000',
                                    borderRadius: 2,
                                    boxShadow: 24,
                                    p: 4,
                                }}>
                                    <Typography class="text-center">
                                        정말 강의실에서 나가시겠습니까?
                                    </Typography>
                                    <div id="modalText">
                                        <Button onClick={leaveSession}>나가기</Button> <Button onClick={modalToggle}>취소</Button>
                                    </div>
                                </Box>
                            </Modal>
                            <Tooltip title="나가기">
                                <PowerSettingsNew/>
                            </Tooltip>
                        </IconButton>
                        {/*채팅창 버튼*/}
                        <IconButton
                            color="inherit"
                            size="large"
                            onClick={toggleChat}
                            id="navChatButton"
                        >
                            {props.showNotification && (
                                <div id="point"/>
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

    export default MatchToolbar;
