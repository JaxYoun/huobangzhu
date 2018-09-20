import React from 'react'
import PropTypes from 'prop-types'
import { Icon, Modal } from 'antd'
import { Iconfont } from '../index'
import styles from './Header.less'
import { Link } from 'react-router'
import Feedback from './components/feedbackModal'
import ModifyPwd from './components/modifyPwdModal'
const confirm = Modal.confirm

class Header extends React.Component {
  state = {
    feedback: {
      visible: false,
    },
    modifyPwd: {
      visible: false,
    },
  }
  handleModal = (type) => {
    let { feedback, modifyPwd } = this.state
    switch (type) {
      case 'feedback':
        Object.assign(feedback, { visible: true })
        break
      case 'modifyPwd':
        Object.assign(modifyPwd, { visible: true })
        break
      default:
        Object.assign(feedback, { visible: false })
        Object.assign(modifyPwd, { visible: false })
        break
    }
    let newModalProps = {
      feedback,
      modifyPwd,
    }
    this.setState(newModalProps)
  }
  handleLogout = () => {
    let { logout } = this.props
    confirm({
      title: '确认退出系统吗?',
      onOk () {
        logout()
      },
    })
  }
  render () {
    let { user, fastLogin, role } = this.props
    let { feedback, modifyPwd } = this.state
    let feedbackProps = {
      ...feedback,
      handleModal: this.handleModal,
    }
    let modifyPwdProps = {
      ...modifyPwd,
      userId: user.id,
      handleModal: this.handleModal,
    }

    return (
      <div>
        <div className={styles.header}>
          {/* <div className={styles.logo}>
            <Link to="/">
              <img src="/logo.png" alt="troy" />
            </Link>
            <span>创意web前端框架</span>
          </div> */}
          <div className={styles.rightWarpper}>
            <div className={styles.button} onClick={fastLogin} >
              <span>  快速登陆</span>
            </div>
            <div className={styles.userButton} onClick={this.handleModal.bind(this, 'modifyPwd')}>
              <Iconfont type="shenfenzheng" />
              <span> {user.name || '--'}</span>
            </div>
            <div className={styles.buttonOut} onClick={this.handleLogout} >
              <Icon type="logout" />
              <span>  退出</span>
            </div>
          </div>
        </div>
        <Feedback {...feedbackProps} />
        <ModifyPwd {...modifyPwdProps} />
      </div>
    )
  }
}

Header.propTypes = {
  role: PropTypes.object,
  user: PropTypes.object,
  logout: PropTypes.func,
  location: PropTypes.object,
  fastLogin: PropTypes.func,
}

export default Header
