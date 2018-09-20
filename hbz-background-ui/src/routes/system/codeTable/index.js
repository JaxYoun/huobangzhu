import React from 'react'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import { Table, Card, Popconfirm, Form, Row, Col, Button, Input } from 'antd'
import { Iconfont, Ttable } from 'components'
import CodeModal from './codeModal'

const FormItem = Form.Item
const CodeTable = ({
  codeTable,
  form,
  dispatch,
}) => {
  const { fields, tableTime, visible, formData, type } = codeTable
  const { getFieldDecorator, resetFields, getFieldsValue } = form
  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  }
  //查询数据字典
  const handleSearch = () => {
    let values = getFieldsValue()
    dispatch({ type: 'codeTable/queryTable', payload: values })
  }
  // 刷新
  const handleUpdate = (record) => {
    record ?
    dispatch({ type: 'codeTable/updateTable', payload: record.dicCode })
    :
    dispatch({ type: 'codeTable/updateTable', payload: {} })
  }
  // 新增
  const handleAdd = () => {
    dispatch({ type: 'codeTable/showModal',payload: { type: 'add' }})
  }
  // 修改数据字典
  const handleEdit = (record) => {
    dispatch({ type: 'codeTable/showModal', payload: { type: 'edit', data: record } })
  }
  // 删除
  const handleDelte = (record) => {
    dispatch({ type: 'codeTable/deleteCode',payload: record.dicCode})
  }
  const columns = [
    {
      title: '操作',
      dataIndex: 'operate',
      render: (text,record) => {
        return (
          <div>
            <span>
              <Button onClick={handleEdit.bind(this, record)}>修改</Button>
            </span>
            <span>
              <Popconfirm
                title="确定删除?"
                onConfirm={handleDelte.bind(this, record)}
              >
                <Button>删除</Button>
              </Popconfirm>
            </span>
            <span>
              <Button onClick={handleUpdate.bind(this, record)}>刷新</Button>
            </span>
          </div>
        )
      }
    },
    {
      title: '字典编码',
      dataIndex: 'dicCode',
    },
    {
      title: '字典值',
      dataIndex: 'dicValue'
    },
    {
      title: '字典排序',
      dataIndex: 'orderCode',
    },
    {
      title: '备注',
      dataIndex: 'memo',
    },
  ]
  return (
    <div>
      <Card title={<span> <Iconfont type="task_fill" /> 查询</span>}>
        <form>
          <Row>
            <Col span="9">
              <FormItem {...formItemLayout} label="字典编码">
                {getFieldDecorator('dicCode', {
                  rules: [{ pattern: /\w+\.\w+\.\w+\.\d$/, message: '请正确输入字典编码!' }],
                })(
                  <Input placeholder="请输入字典编码" />
                )}
              </FormItem>
            </Col>
            <Col span="9" offset="6">
              <Button
                type="primary"
                icon="search"
                onClick={handleSearch.bind(this)}
              >
                查询
              </Button>
              <Button
                type="primary"
                icon="reload"
                onClick={handleUpdate.bind(this)}
              >
                全部更新
              </Button>
            </Col>
          </Row>
        </form>
      </Card>
      <Card title={<span> <Iconfont type="task_fill" /> 数据字典</span>}
        extra={
          <Button  type="primary" onClick={handleAdd.bind(this)}>新增</Button>
        }
      >
        <Ttable
          columns={columns}
          sourceUrl="/system/codeTable/getPage"
          fields={fields}
          tableTime={tableTime}
          serialNumber
        />
      </Card>
      {
        visible && <CodeModal visible={visible}
          dispatch={dispatch} formData={formData}
          type={type}
        />
      }
    </div>
  )
}
CodeTable.propTypes = {
  codeTable: PropTypes.object.isRequired,
  app: PropTypes.object.isRequired,
  form: PropTypes.isRequired, 
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ app, codeTable }) => ({ app, codeTable }))(
  Form.create()(CodeTable)
  )