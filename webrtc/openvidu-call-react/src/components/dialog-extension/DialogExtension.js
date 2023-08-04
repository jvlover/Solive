import React, {Component} from 'react';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import './DialogExtension.css';

// 화면 공유 창
export default class DialogExtensionComponent extends Component {
    constructor(props) {
        super(props);
        this.openviduExtensionUrl =
            'https://chrome.google.com/webstore/detail/openvidu-screensharing/lfcgfepafnobdloecchnfaclibenjold';
        //isInstalled: boolean;

        this.state = {
            isInstalled: false,
        };
        this.goToChromePage = this.goToChromePage.bind(this);
        this.onNoClick = this.onNoClick.bind(this);
        this.refreshBrowser = this.refreshBrowser.bind(this);
    }

    componentWillReceiveProps(props) {
    }

    componentDidMount() {
    }

    onNoClick() {
        // this.cancel.emit();
        this.props.cancelClicked();
    }

    goToChromePage() {
        window.open(this.openviduExtensionUrl);
        this.setState({isInstalled: true});
    }

    refreshBrowser() {
        window.location.reload();
    }

    render() {
        return (
            <div>
                {this.props && this.props.showDialog ? (
                    <div id="dialogExtension">
                        <Card id="card">
                            <CardContent>
                                <Typography
                                    color="textSecondary">안녕하세요</Typography>
                                <Typography color="textSecondary">
                                    화면을 공유하기 위해서는 크롬 확장앱을 설치한 후 새로고침을 하셔야합니다.
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Button size="small" onClick={this.onNoClick}>
                                    취소
                                </Button>

                                <Button size="small"
                                        onClick={this.goToChromePage}>
                                    설치
                                </Button>
                                {this.state.isInstalled ? (
                                    <Button size="small"
                                            onClick={this.refreshBrowser}>
                                        새로고침
                                    </Button>
                                ) : null}
                            </CardActions>
                        </Card>
                    </div>
                ) : null}
            </div>
        );
    }
}
