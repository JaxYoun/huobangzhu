import React from 'react'
import PropTypes from 'prop-types'
import { Form, Row, Col, Button, Input, Tabs } from 'antd'
import { request, config, regex } from 'utils'

import styles from './index.less'

const FormItem = Form.Item
const TabPane = Tabs.TabPane
const { api } = config

const PwdModify = (params) => {
  const { confirmLoading, form, updateState, smsWait, steps, getSms, handleLogin, token } = params
  const { getFieldDecorator, validateFields, getFieldValue } = form

  const onSubmit = () => {
    validateFields((errors, values) => {
      if (errors) {
        return
      }
      updateState({
        confirmLoading: true,
      })
      request(api.resetPwd, {
        headers: { 'X-AUTH-TOKEN': token },
        body: JSON.stringify(values),
      }).then((json) => {
        setTimeout(() => {
          updateState({
            confirmLoading: false,
          })
          if (json.success) {
            handleLogin({
              modalType: 'login',
            })
            updateState({
              steps: '1',
            })
          }
        }, 100)
      })
    })
  }

  const telephoneChecked = (rule, value, callback) => {   // 验证手机号码是否合格，是否可用，是否被注册
    if (value && !new RegExp(regex.phone).test(value)) {
      callback('请输入正确的手机格式')
      return
    }
    callback()
  }
  const toNext = () => {  // 下一步
    validateFields(['telephone', 'authCode'], (errors, values) => {
      if (errors) {
        return
      }
      updateState({
        confirmLoading: true,
      })
      request(api.authCodeCheck, {
        body: JSON.stringify({ authCode: values.authCode }),
      }).then((json) => {
        setTimeout(() => {
          if (json.success) {
            updateState({
              steps: '2',
            })
          }
          updateState({
            confirmLoading: false,
          })
        }, 100)
      })
    })
  }
  const handleConfirmPassword = (rule, value, callback) => {
    if (value && value !== getFieldValue('password')) {
      callback('两次输入不一致！')
      return
    }
    callback()
  }
  return (<Form className={styles['pwd-modify-form']} >
    <Tabs activeKey={steps} tabPosition="left" >
      <TabPane key="1" >
        <FormItem>
          {getFieldDecorator('telephone', {
            rules: [
              {
                required: true,
                message: '请输入手机号码',
              },
              { validator: telephoneChecked },
            ],
          })(
            <Input size="large" onPressEnter={getSms.bind(this, form)} placeholder="手机号码" />
          )}
        </FormItem>
        <FormItem className={styles['pwd-modify-input']}>
          {getFieldDecorator('authCode', {
            rules: [
              {
                required: true,
                message: '请输入验证码',
              },
            ],
          })(
            <Row>
              <Col span="14">
                <Input
                  size="large"
                  type="text"
                  onPressEnter={toNext}
                  placeholder="验证码"
                />
              </Col>
              <Col span="10">
                <Button
                  className={styles['get-sms']}
                  type="primary"
                  onClick={getSms.bind(this, form)}
                  loading={!(smsWait <= 0)}
                >
                  {`获取验证码${smsWait ? `(${smsWait}s)` : ''}`}
                </Button>
              </Col>
            </Row>
          )}
        </FormItem>
        <Row className={styles['sub-btn']} >
          <Button
            type="primary"
            onClick={toNext}
            loading={confirmLoading}
          >
            提 交
          </Button>
        </Row>
      </TabPane>
      <TabPane key="2" >
        <FormItem >
            {getFieldDecorator('password', {
              rules: [
                { required: true, message: '请输入密码' },
                { min: 6, message: '密码不能小于6位' },
                { max: 16, message: '密码不能大于16位' },
              ],
            })(<Input type="password" placeholder="密码" onPressEnter={onSubmit} />)}
        </FormItem>
        <FormItem >
            {getFieldDecorator('confirmPassword', {
              rules: [
                { required: true, message: '请输入确认密码' },
                { min: 6, message: '确认密码不能小于6位' },
                { max: 16, message: '确认密码字数不能大于16位' },
                { validator: handleConfirmPassword },
              ],
            })(<Input type="password" placeholder="确认密码" onPressEnter={onSubmit} />)}
        </FormItem>
        <Row className={styles['sub-btn']} >
          <Button
            type="primary"
            onClick={onSubmit}
            loading={confirmLoading}
          >
            确 定
          </Button>
        </Row>
      </TabPane>
    </Tabs>
  </Form>)
}

PwdModify.propTypes = {
  form: PropTypes.object,
  param: PropTypes.object,
}

const PwdModifyForm = Form.create()(PwdModify)

export default PwdModifyForm
