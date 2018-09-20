import React from 'react'
import PropTypes from 'prop-types'
import { Form, Row, Col, Button, Input } from 'antd'
import { request, config, COOKIE } from 'utils'

import styles from './index.less'

const FormItem = Form.Item
const { api } = config

const Login = (params) => {
  const {
    form,
    onCancel,
    queryMenus,
    pathname,
    queryPersonalCenter,
    updateState,
    handleLogin,
    authCodeImg,
    getAuthCode,
    confirmLoading,
  } = params
  const { getFieldDecorator, validateFieldsAndScroll } = form
  const doLogin = () => {
    validateFieldsAndScroll((errors, values) => {
      if (errors) {
        return
      }
      updateState({
        confirmLoading: true,
      })
      request(api.userLogin, {
        body: JSON.stringify({
          ...values,
          loginFor: 'Web',
          language: 'zh_CN',
        }),
      }).then((json) => {
        setTimeout(() => {
          updateState({
            confirmLoading: false,
          })
          if (json.success) {
            sessionStorage.isvisited = true
            COOKIE.setCookie('X-AUTH-TOKEN', json.data['X-AUTH-TOKEN'])
            COOKIE.setCookie('X-AUTH-USER', json.data['X-AUTH-USER'])
            queryMenus()
            onCancel()
            if (pathname === '/personalCenter') {
              queryPersonalCenter()
            }
          } else {
            let loginFailTimes = COOKIE.getCookie('loginFailTimes')
            if (loginFailTimes >= 0) {
              loginFailTimes++
            } else {
              loginFailTimes = 0
            }
            COOKIE.setCookie('loginFailTimes', loginFailTimes, 10)
            loginFailTimes >= 3 && getAuthCode()
          }
        }, 100)
      })
    })
  }
  const loginFailTimes = COOKIE.getCookie('loginFailTimes')
  // // COOKIE.clearCookie()
  // // COOKIE.setCookie('aa', '1111', 9)
  // console.log(COOKIE.getCookie('aa'))
  return (<Form>
    <FormItem >
      {getFieldDecorator('j_user', {
        rules: [
          {
            required: true,
            message: '请输入登录账号',
          },
        ],
      })(
        <Input size="large" onPressEnter={doLogin} placeholder="登录账号" />
      )}
    </FormItem>
    <FormItem >
      {getFieldDecorator('j_pass', {
        rules: [
          {
            required: true,
            message: '请输入登录密码',
          },
        ],
      })(
        <Input
          size="large"
          type="password"
          onPressEnter={doLogin}
          placeholder="登录密码"
        />
      )}
    </FormItem>
    {loginFailTimes >= 3
    && (<FormItem className={'validate-code-login validate-code'}>
      {getFieldDecorator('authCode', {
        rules: [
          {
            required: true,
            message: '请输入验证码',
          },
        ],
      })(
        <Row>
          <Col span={12}>
            <Input
              size="large"
              type="text"
              onPressEnter={doLogin}
              placeholder="验证码"
            />
          </Col>
          <Col span={12} >
            <img src={`data:image/png;base64,${authCodeImg}`} onClick={getAuthCode} alt="验证码君" />
          </Col>
        </Row>
      )}
    </FormItem>)
    }

    <Row className={styles['sub-btn']} style={(loginFailTimes >= 3) ? {} : { 'margin-top': '50px' }} >
      <Button
        className={styles['sub-btn-login']}
        type="primary"
        onClick={doLogin}
        loading={confirmLoading}
      >
        登 录
      </Button>
      <a className={styles['forget-pwd-btn']} onClick={handleLogin.bind(this, { modalType: 'pwdModify' })}>忘记密码</a>
      <a className={styles['to-regist-btn']} onClick={handleLogin.bind(this, { modalType: 'register' })}>去注册</a>
    </Row>
  </Form>)
}

Login.propTypes = {
  form: PropTypes.object,
  param: PropTypes.object,
}

const LoginForm = Form.create()(Login)

export default LoginForm
