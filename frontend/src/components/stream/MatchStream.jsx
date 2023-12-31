import React, {Component} from 'react';
import './MatchStream.css';
import OvVideoComponent from './OvVideo.jsx';

import MicOff from '@mui/icons-material/MicOff';
import VideocamOff from '@mui/icons-material/VideocamOff';
import VolumeUp from '@mui/icons-material/VolumeUp';
import VolumeOff from '@mui/icons-material/VolumeOff';
import IconButton from '@mui/material/IconButton';

// 나 혹은 구독자의 stream 이 뜨는 창
export default class MatchStream extends Component {
    constructor(props) {
        super(props);
        this.state = {
            nickname: this.props.user.getNickname(),
            showForm: false,
            mutedSound: false,
            isFormValid: true
        };
        this.toggleSound = this.toggleSound.bind(this);
    }

    toggleSound() {
        this.setState({mutedSound: !this.state.mutedSound});
    }

    render() {
        return (
            <div className="OT_widget-container">
                {/*여기서 닉네임 보여줌*/}
                <div className="nickname">
                    {this.state.nickname}
                </div>

                {this.props.user !== undefined
                && this.props.user.getStreamManager() !== undefined ? (
                    <div className="streamComponent">
                        <OvVideoComponent user={this.props.user}
                                          mutedSound={this.state.mutedSound}/>
                        <div id="statusIcons">
                            {!this.props.user.isVideoActive() ? (
                                <div id="camIcon">
                                    <VideocamOff id="statusCam"/>
                                </div>
                            ) : null}

                            {!this.props.user.isAudioActive() ? (
                                <div id="micIcon">
                                    <MicOff id="statusMic"/>
                                </div>
                            ) : null}
                        </div>
                        <div>
                            {/*로컬유저 아니면 = 나 아니면 음소거버튼 뜨게*/}
                            {!this.props.user.isLocal() && (
                                <IconButton id="volumeButton"
                                            onClick={this.toggleSound}>
                                    {this.state.mutedSound ? <VolumeOff
                                        color="secondary"/> : <VolumeUp/>}
                                </IconButton>
                            )}
                        </div>
                    </div>
                ) : null}
            </div>
        );
    }
}
