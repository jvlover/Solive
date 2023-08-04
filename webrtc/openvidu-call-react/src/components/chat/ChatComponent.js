import React, {Component} from "react";
import IconButton from "@material-ui/core/IconButton";
import Fab from "@material-ui/core/Fab";
import HighlightOff from "@material-ui/icons/HighlightOff";
import Send from "@material-ui/icons/Send";

import "./ChatComponent.css";
import {Tooltip} from "@material-ui/core";
// 여기 지금은 그냥 성호 사진 넣었는데 나중에 유저 사진 받아오는 걸로 바꾸자
import student from "../../assets/images/student.jpg";

export default class ChatComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            messageList: [],
            message: "",
            messageTime: null,
        };
        this.chatScroll = React.createRef();
        this.handleChange = this.handleChange.bind(this);
        this.handlePressKey = this.handlePressKey.bind(this);
        this.close = this.close.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
    }

    componentDidMount() {
        this.props.user
        .getStreamManager()
        .stream.session.on("signal:chat", (event) => {
            const data = JSON.parse(event.data);
            let messageList = this.state.messageList;
            // 이게 전해지는 메시지
            messageList.push({
                connectionId: event.from.connectionId,
                nickname: data.nickname,
                message: data.message,
                messageTime: data.messageTime,
            });
            setTimeout(() => {
                this.props.messageReceived();
            }, 50);
            // 0.05초 있다가 내용물 시행하는 setTimeout
            this.setState({messageList: messageList});
            this.scrollToBottom();
            // 메시지 올리고 스크롤 바닥으로
        });
    }

    handleChange(event) {
        this.setState({message: event.target.value});
    }

    handlePressKey(event) {
        if (event.key === "Enter") {
            this.sendMessage();
        }
    }

    sendMessage() {
        if (this.props.user && this.state.message) {
            let message = this.state.message.replace(/ +(?= )/g, "");
            let messageTime = new Date();
            // 문자열에서 연속된 공백을 찾고, 그러한 연속된 공백을 하나의 공백으로 대체
            if (message !== "" && message !== " " && messageTime !== null) {
                const data = {
                    message: message,
                    nickname: this.props.user.getNickname(),
                    streamId:
                    this.props.user.getStreamManager().stream.streamId,
                    messageTime: messageTime,
                };
                this.props.user.getStreamManager().stream.session.signal({
                    data: JSON.stringify(data),
                    type: "chat",
                });
            }
        }
        this.setState({message: "", messageTime: null});
    }

    scrollToBottom() {
        setTimeout(() => {
            try {
                this.chatScroll.current.scrollTop =
                    this.chatScroll.current.scrollHeight;
            } catch (err) {
            }
        }, 20);
    }

    close() {
        this.props.close(undefined);
    }

    render() {
        const styleChat = {display: this.props.chatDisplay};
        return (
            <div id="chatContainer">
                <div id="chatComponent" style={styleChat}>
                    <div id="chatToolbar">
                        <span>채팅창</span>
                        {/* 이건 채팅창 닫기 버튼 */}
                        <IconButton id="closeButton" onClick={this.close}>
                            <HighlightOff color="secondary"/>
                        </IconButton>
                    </div>
                    <div className="message-wrap" ref={this.chatScroll}>
                        {this.state.messageList.map((data, i) => (
                            // connectionId가 user아이디랑 다르면 왼쪽에 같으면 오른쪽에 채팅을 띄운다
                            <div
                                key={i}
                                id="remoteUsers"
                                className={
                                    "message" +
                                    (data.connectionId !==
                                    this.props.user.getConnectionId()
                                        ? " left"
                                        : " right")
                                }
                            >
                                {/* 실제로 사진이 들어가는 곳 */}
                                <img
                                    src={student}
                                    alt="face"
                                    className="user-img"
                                />
                                <div className="msg-detail">
                                    <div className="msg-info">
                                        <p> {data.nickname}</p>
                                    </div>
                                    <div className="msg-content">
                                        <span className="triangle"/>
                                        <p className="text">{data.message}</p>
                                        <p className="text">{data.messageTime}</p>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div id="messageInput">
                        <input
                            placeholder="메시지를 입력하세요"
                            id="chatInput"
                            value={this.state.message}
                            onChange={this.handleChange}
                            onKeyPress={this.handlePressKey}
                        />
                        <Tooltip title="메시지를 전송합니다.">
                            <Fab
                                size="small"
                                color="primary"
                                id="sendButton"
                                onClick={this.sendMessage}
                            >
                                <Send/>
                            </Fab>
                        </Tooltip>
                    </div>
                </div>
            </div>
        );
    }
}
