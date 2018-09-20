import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button, Switch, Popconfirm } from 'antd'
import { Iconfont, Ttable } from 'components'
import { regex } from 'utils'
import styles from './index.less'
import AccountDetail from './accountDetail'

const FormItem = Form.Item

const AccountManage = ({
  dispatch,
  accountManage,
  form,
  loading,
}) => {
  const { getFieldDecorator } = form
  const { fields, tableTime, availableRole, visible, accountDetail, orgData } = accountManage
  const rest = () => {
    form.resetFields()
  }
  const switchActive = (id, checked) => {
    dispatch({
      type: 'accountManage/isEnable',
      payload: {
        id,
        checked,
      },
    })
  }
  const showRoles = (roles) => {
    let show = []
    for (let item of roles) {
      for (let items of availableRole) {
        if (item === items.id) {
          show.push(items.roleName)
        }
      }
    }
    if (show.length === 0) {
      show.push('暂无角色')
    }
    return show.join(',')
  }
  const checkDetail = (data) => {
    dispatch({
      type: 'accountManage/changeStates',
      payload: {
        visible: true,
        accountDetail: data,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'accountManage/changeStates',
      payload: {
        accountDetail: '',
        visible: true,
      },
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'accountManage/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const confirmDelete = (data) => {
    dispatch({
      type: 'accountManage/DeletaUser',
      payload: {
        id: data.id,
      },
    })
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card
            className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="jiaose" className={styles.send} />
                <span>{record.nickName}</span>
              </div>
            }
            // onClick={checkDetail.bind(this, record)}
            extra={
              <div>
                <Switch
                  checkedChildren="启用"
                  unCheckedChildren="停用"
                  checked={record.activated}
                  onChange={switchActive.bind(this, record.id)}
                />
                <Button type="primary" onClick={checkDetail.bind(this, record)} style={{ marginLeft: '10px' }}>编 辑</Button>
                <Popconfirm title="确认删除吗?" onConfirm={confirmDelete.bind(this, record)} >
                  <Button type="danger">删除</Button>
                </Popconfirm>
              </div>
            }
          >
            <div className={styles.content}>
              <span className={styles.name}>联系电话:</span>
              <span className={styles.value}>{record.telephone}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>所属部门:</span>
              <span className={styles.value}>{record.org.organizationName}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>所属角色:</span>
              <span className={styles.value}>{showRoles(record.roleIds)}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  const queryAccount = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({
        type: 'accountManage/changeStates',
        payload: {
          fields: formValue,
        },
      })
    })
  }
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/enterprise/user/queryPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const modalProps = {
    visible,
    hideModal,
    availableRole,
    dispatch,
    loading,
    accountDetail,
    orgData,
  }
  return (
    <div>
      <Card title="查询账户">
        <Row>
          <Col span={7}>
            <FormItem label="登录名">
                {getFieldDecorator('login', {
                })(
                  <Input placeholder="请输入搜索账户的登录名" />
                )}
            </FormItem>
          </Col>
          <Col span={7} offset={1}>
            <FormItem label="手机号">
                {getFieldDecorator('telephone', {
                  rules: [
                    { pattern: regex.telephone, message: '请填写正确的手机号码' },
                  ],
                })(
                  <Input placeholder="请输入搜索账户的手机号" />
                )}
            </FormItem>
          </Col>
          <Col span={7} offset={1}>
            <FormItem label="昵称">
                {getFieldDecorator('nickName', {
                })(
                  <Input placeholder="请输入搜索账户的昵称" />
                )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <Button type="primary" onClick={queryAccount} style={{ marginTop: '34px' }}>查询</Button>
            <Button type="primary" onClick={rest} style={{ marginTop: '34px' }}>重置</Button>
          </Col>
        </Row>
      </Card>
      <Card
        extra={<Button type="primary" onClick={showModal}>新 增</Button>}
      >
        <Ttable {...tableProps} />
      </Card>
      {
        visible && <AccountDetail {...modalProps} />
      }
    </div>
  )
}

AccountManage.propTypes = {
  form: PropTypes.isRequired,
  accountManage: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  loading: PropTypes.isRequired,
}
const AccountManageForm = Form.create()(AccountManage)
export default connect(({ accountManage, loading }) => ({ accountManage, loading }))(AccountManageForm)
