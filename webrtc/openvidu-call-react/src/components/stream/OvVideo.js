import React, {Component} from 'react';
import './StreamComponent.css';

export default class OvVideoComponent extends Component {
    constructor(props) {
        super(props);
        this.videoRef = React.createRef();
    }

    componentDidMount() {
// videoRef와 내려받은 props, user의 streamManager가 있을 때(session은 없어도 됨) -> streamManager에 videoElement 저장해준다.
// !! 는 undefined나 "", null 아니면 무조건 true
        if (this.props && this.props.user.streamManager && !!this.videoRef) {
            console.log('이건 ovVideo PROPS: ', this.props);
            console.log('이건 ovVideo videoRef: ', this.videoRef);
            this.props.user.getStreamManager().addVideoElement(
                this.videoRef.current);
        }
// videoRef와 내려 받은 props가 있고 거기에 user와 user의 session가 있을 때만!
        if (this.props && this.props.user.streamManager.session
            && this.props.user && !!this.videoRef) {
            this.props.user.streamManager.session.on('signal:userChanged',
                (e) => {
                    const data = JSON.parse(e.data);
                    console.log("이건 데이터입니다" + data);
                    // 화면공유 undefined가 아닐 때 -> streamManager에 videoElement 저장해준다.
                    if (data.isScreenShareActive !== undefined) {
                        this.props.user.getStreamManager().addVideoElement(
                            this.videoRef.current);
                    }
                });
        }
    }

    componentDidUpdate(props) {
        if (props && !!this.videoRef) {
            this.props.user.getStreamManager().addVideoElement(
                this.videoRef.current);
        }
    }

    render() {
        return (
            <>
                <div>어디냐고</div>
                <video
                    autoPlay={true}
                    id={'video-'
                        + this.props.user.getStreamManager().stream.streamId}
                    // streamId는 어디서 나오지?
                    ref={this.videoRef}
                    muted={this.props.mutedSound}
                />
                <div>이건 어디야</div>
            </>
        );
    }
}