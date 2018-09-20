import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Button, Row, Form, Input } from 'antd'
import { config } from '../../utils'
import PostModal from './postModal.js'
import styles from './index.less'

const FormItem = Form.Item

const Login = ({
  login,
  dispatch,
  form: { getFieldDecorator, validateFieldsAndScroll },
}) => {
  const { loginLoading, visible, posts } = login

  function handleOk () {
    validateFieldsAndScroll((errors, values) => {
      if (errors) {
        return
      }
      dispatch({ type: 'login/login', payload: values })
    })
  }

  return (
    <div>
      <img alt={'logo'} src={config.logo2} className={styles.loginimg} />
      <div className={styles.form}>
        <div className={styles.logo}>
          <span>{config.name}</span>
        </div>
        <form>
          <FormItem hasFeedback>
            {getFieldDecorator('loginName', {
              rules: [
                {
                  required: true,
                  message: '请输入登录账号',
                },
              ],
            })(
              <Input style={{ height: '32px' }} size="large" onPressEnter={handleOk} placeholder="登录账号" />
            )}
          </FormItem>
          <FormItem hasFeedback>
            {getFieldDecorator('password', {
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
                onPressEnter={handleOk}
                placeholder="登录密码"
                style={{ height: '32px' }}
              />
            )}
          </FormItem>
          <Row>
            <Button
              type="primary"
              size="large"
              onClick={handleOk}
              loading={loginLoading}
            >
              登 录
            </Button>
            <p>
              <span>@copyright TROY 创意信息</span>
            </p>
          </Row>
        </form>
        <PostModal visible={visible} posts={posts} dispatch={dispatch} />
      </div>
    </div>
  )
}

Login.propTypes = {
  form: PropTypes.isRequired,
  login: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ login }) => ({ login }))(Form.create()(Login))
