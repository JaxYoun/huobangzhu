import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import { COOKIE } from 'utils'
import styles from './index.less'

const registry = {
  UN_DO: '未注册',
  UN_REGISTER: '审核中',
  REGISTRYED: '已注册',
  ERR_REGISTER: '注册失败',
}
const showText = {
  UN_DO: '点击注册',
  UN_REGISTER: '',
  REGISTRYED: '点击修改',
  ERR_REGISTER: '请点击重新注册',
}
const PersonalCenter = ({
  personalCenter,
  dispatch,
}) => {
  const { userInfo, register } = personalCenter
  const onClick = (text) => {
    console.log(text)
    if (text === 'ower') {
      if (register.EnterpriseConsignor === 'REGISTRYED') {
        dispatch({
          type: 'personalCenter/queryRegistry',
          payload: {
            questFields: {
              registryCode: 'EnterpriseConsignor',
            },
            parent: 'enterpriseConsignor',
          },
        })
      } else if (register.EnterpriseConsignor === 'UN_REGISTER') {
        message.warn('正在审核中，请稍后')
        return
      } else {
        dispatch({
          type: 'personalCenter/updateOther',
          payload: {
            iDcardImage: '',
            licenceImage: '',
            registryData: '',
            parent: 'enterpriseConsignor',
          },
        })
      }
      browserHistory.push('/personalCenter/authentication/enterpriseConsignor')
    } else {
      if (register.TransEnterprise === 'REGISTRYED') {
        dispatch({
          type: 'personalCenter/queryRegistry',
          payload: {
            questFields: {
              registryCode: 'TransEnterprise',
            },
            parent: 'transEnterprise',
          },
        })
      } else if (register.TransEnterprise === 'UN_REGISTER') {
        message.warn('正在审核中，请稍后')
        return
      } else {
        dispatch({
          type: 'personalCenter/updateOther',
          payload: {
            iDcardImage: '',
            licenceImage: '',
            registryData: '',
            parent: 'transEnterprise',
          },
        })
      }
      browserHistory.push('/personalCenter/authentication/transEnterprise')
    }
  }
  const changePersonInfo = () => {
    browserHistory.push('/personalCenter/personalInfo')
  }
  return (
    <div className={styles.center}>
      <Card>
        <Row>
          <Col>
            <div className={styles['head-image']}>
              <img alt="照片" src={userInfo.imageUrl} onClick={changePersonInfo} />
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
            <div className={styles['user-name']}>{(userInfo && userInfo.nickName) ? userInfo.nickName : '暂无昵称'}</div>
          </Col>
        </Row>
        <Row>
          <Col>
            <div className={styles['user-account']}>
              <Iconfont type="wode" className={styles['account-icon']} />
              <span>账号:</span>
              <span>{userInfo && userInfo.login}</span>
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
            <div className={styles['user-number']}>
              <Iconfont type="jifen" className={styles['number-icon']} />
              <span>积分:</span>
              <span>{userInfo && userInfo.starLevel}</span>
            </div>
          </Col>
        </Row>
      </Card>
      <Card title="用户认证">
        <ul className={styles['user-authentication']}>
          <li>
            <div
              className={register.EnterpriseConsignor === 'REGISTRYED' ? styles.registered : styles['un-registered']}
              onClick={onClick.bind(this, 'ower')}
            >
              <p>货主企业</p>
              {/* <p>
                {
                  register.EnterpriseConsignor === 'REGISTRYED' ? '点击修改' : '点击认证'
                }
              </p> */}
              <p>
                {
                  registry[register.EnterpriseConsignor]
                }
              </p>
              <p>
                {
                  showText[register.EnterpriseConsignor]
                }
              </p>
            </div>
          </li>
          <li>
            <div
              className={register.TransEnterprise === 'REGISTRYED' ? styles.registered : styles['un-registered']}
              onClick={onClick.bind(this, 'trans')}
            >
              <p>运输企业</p>
              <p>
                {
                  registry[register.TransEnterprise]
                }
              </p>
              <p>
                {
                  showText[register.TransEnterprise]
                }
              </p>
            </div>
          </li>
        </ul>
      </Card>
    </div>
  )
}

PersonalCenter.propTypes = {
  personalCenter: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ personalCenter }) => ({ personalCenter }))(PersonalCenter)
