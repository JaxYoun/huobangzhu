import React from 'react'
import PropTypes from 'prop-types'
import { Modal } from 'antd'
import { request, config, COOKIE } from 'utils'

import styles from './index.less'

import Login from './login'
import Register from './register'
import PwdModify from './pwdModify'

const { api } = config
const modalTypeObj = {
  login: { content: Login, name: '登陆', nameEn: 'LOGIN' },
  register: { content: Register, name: '注册', nameEn: 'REGISTER' },
  pwdModify: { content: PwdModify, name: '密码修改', nameEn: 'PASSWORD modification' },
}

class ModalLogin extends React.Component {
  state = {
    confirmLoading: false,
    authCodeImg: '',
    smsWait: '',
    steps: '1', // 步骤
    registProtVisible: false,
    token: '',
  }
  componentDidMount () {
    this.getToken()
    // this.getAuthCode()
  }
  getAuthCode = () => {   // 获取验证码图片
    request(api.authCodePeek).then((json) => {
      if (json.success) {
        this.setState({ authCodeImg: json.data })
      }
    })
  }
  getToken = () => {
    const token = COOKIE.getCookie('X-AUTH-TOKEN')
    const { modalType } = this.props
    if (modalType === 'login') {
      request(api.token).then((json) => {
        if (json.success) {
          COOKIE.clearCookie()
          COOKIE.setCookie('X-AUTH-TOKEN', json.data)
        }
      })
    }
  }
  getSms = (form) => {
    const { token } = this.state
    const { validateFields } = form
    validateFields(['telephone'], {}, (errors, values) => {
      if (errors) {
        return
      }
      this.setState({
        smsWait: 60,
      })
      request(api.authCodeSend, {
        headers: {
          'X-AUTH-TOKEN': token,
        },
        body: JSON.stringify({ phone: values.telephone }),
        showMsg: false,
      }).then((json) => {
        setTimeout(() => {
          if (json.success) {
            let smsWaitTime = 60
            let waiteTimeval = setInterval(() => {
              smsWaitTime >= 1
              ? this.setState({
                smsWait: --smsWaitTime,
              }) : clearInterval(waiteTimeval)
            }, 1000)
          }
        }, 100)
      })
    })
  }
  updateState = (nextStae) => {
    this.setState(nextStae)
  }
  render () {
    const { visible, onCancel, modalType, handleLogin, queryMenus, pathname, queryPersonalCenter } = this.props
    const ModalObj = modalTypeObj[modalType]  //  根据modalType 获取对应内容
    const modalProps = {
      // closable: false,
      visible,
      maskClosable: false,
      key: visible,
      width: 900,
      footer: null,
      bodyStyle: { padding: '0' },
      maskStyle: { 'background-color': 'rgba(55, 55, 55, 0.6)' },
      wrapClassName: 'login-modal-wrap',
      onCancel,
    }
    const loginProps = {
      ...this.state,
      updateState: this.updateState,
      onCancel,
      handleLogin,
      getSms: this.getSms,
      getAuthCode: this.getAuthCode,
      queryMenus,
      pathname,
      queryPersonalCenter,
    }
    return (
      <Modal {...modalProps} >
        <div className={styles['login-box']} >
          <div className={styles['login-bg']}>
            <div className={styles['bg-title']} >
              <span className={styles['bg-title-logo']}><img src="/hbz_logo.png" alt="货帮主logo" /></span>
              <span>货帮组物流平台</span>
            </div>
            <div className={styles['bg-content']} >
            成都货帮主网络科技有限公司成立于国家“互联网+”行动计划开局之年，致力于以互联网为依托下的运输能力资源池 ，打造成为高效率高水准的运输能力提供商。
            </div>
          </div>
          <div className={styles.form}>
            <div className={styles['m-name']}>
              <span>{ModalObj.name}</span>
              <span>{ModalObj.nameEn}</span>
            </div>
            <ModalObj.content {...loginProps} />
          </div>
        </div>
      </Modal>
    )
  }
}

ModalLogin.propTypes = {
  visible: PropTypes.bool,
  form: PropTypes.object,
  modalType: PropTypes.string,
  handleModal: PropTypes.func,
  userId: PropTypes.string,
  dispatch: PropTypes.func,
  onCancel: PropTypes.func,
  handleLogin: PropTypes.func,
}
export default ModalLogin
