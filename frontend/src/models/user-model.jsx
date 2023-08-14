class UserModel {
    connectionId;
    audioActive;
    videoActive;
    screenShareActive;
    nickname;
    picture;
    streamManager;
    type; // 'remote' | 'local'

    // 여기서 방 입장했을 때 기본 설정사항 정할 수 있다.
    constructor() {
        this.connectionId = "";
        this.audioActive = false;
        this.videoActive = false;
        this.screenShareActive = false;
        this.nickname = "";
        this.picture = "";
        this.streamManager = null;
        this.type = "local";
    }

    isAudioActive() {
        return this.audioActive;
    }

    isVideoActive() {
        return this.videoActive;
    }

    isScreenShareActive() {
        return this.screenShareActive;
    }

    getConnectionId() {
        return this.connectionId;
    }

    getNickname() {
        return this.nickname;
    }

    getPicture() {
        return this.picture;
    }

    getStreamManager() {
        return this.streamManager;
    }

    isLocal() {
        return this.type === "local";
    }

    isRemote() {
        return !this.isLocal();
    }

    setAudioActive(isAudioActive) {
        this.audioActive = isAudioActive;
    }

    setVideoActive(isVideoActive) {
        this.videoActive = isVideoActive;
    }

    setScreenShareActive(isScreenShareActive) {
        this.screenShareActive = isScreenShareActive;
    }

    setStreamManager(streamManager) {
        this.streamManager = streamManager;
    }

    setConnectionId(conecctionId) {
        this.connectionId = conecctionId;
    }

    setNickname(nickname) {
        this.nickname = nickname;
    }

    setPicture(picture){
        this.picture = picture;
    }

    setType(type) {
        if ((type === "local") | (type === "remote")) {
            this.type = type;
        }
    }
}

export default UserModel;
