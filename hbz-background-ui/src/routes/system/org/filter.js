import React from 'react'
import { Form, Input, TreeSelect, Select, Row, Col, Button } from 'antd'
import PropTypes from 'prop-types'

const FormItem = Form.Item
const Option = Select.Option

const OrgForm = ({
  orgData,
  dispatch,
  form,
}) => {
  const { getFieldDecorator } = form
  const handleReset = () => {
    form.resetFields()
  }
  const handleSubmit = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      console.log(formValues)
      dispatch({ type: 'org/queryUser', payload: formValues })
    })
  }
  const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 14 },
  }
  return (
    <Form>
      <Row type="flex" align="middle">
        <Col span={10}>
          <FormItem {...formItemLayout} label="用户姓名">
            {getFieldDecorator('userName', {
            })(
              <Input placeholder="用户姓名" />
            )}
          </FormItem>
          <FormItem {...formItemLayout} label="用户状态">
            {getFieldDecorator('activated', {
            })(
              <Select placeholder="用户状态">
                <Option value={1}>启用</Option>
                <Option value={0}>停用</Option>
              </Select>
            )}
          </FormItem>
        </Col>
        <Col span={10}>
          <FormItem {...formItemLayout} label="机构名称">
            {getFieldDecorator('orgId', {
            })(
              <TreeSelect
                showSearch
                treeDataSimpleMode
                treeNodeFilterProp="label"
                treeData={orgData}
                placeholder="机构名称"
              />
            )}
          </FormItem>
          <FormItem {...formItemLayout} label="电话号码">
            {getFieldDecorator('mobilePhone', {
            })(
              <Input placeholder="电话号码" />
            )}
          </FormItem>
        </Col>
        <Col span={4}>
          <Button icon="reload" style={{ float: 'right' }} onClick={handleReset}>重置</Button>
          <Button icon="search" style={{ float: 'right' }} onClick={handleSubmit}>查询</Button>
        </Col>
      </Row>
    </Form>
  )
}

const OrgFilter = Form.create()(OrgForm)

OrgForm.propTypes = {
  form: PropTypes.obj,
  orgData: PropTypes.array,
  dispatch: PropTypes.func,
}

export default OrgFilter
