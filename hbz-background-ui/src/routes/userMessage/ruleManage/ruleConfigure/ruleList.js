import React from 'react'
import { Card, Button, Popconfirm, Switch } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import RuleModal from './ruleModal'
import config from 'config'

const { state, adjust, type } = config

const RuleList = ({
  dispatch,
  fields,
  tableTime,
  visible,
  adjusts,
  states,
  types,
  ruleDetail,
  loading,
}) => {
  const showModal = (data) => {
    dispatch({
      type: 'ruleManage/changeStates',
      payload: {
        visible: true,
        ruleDetail: data,
        parent: 'ruleConfigure',
      },
    })
  }
  const addRule = () => {
    dispatch({
      type: 'ruleManage/changeStates',
      payload: {
        visible: true,
        ruleDetail: '',
        parent: 'ruleConfigure',
      },
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'ruleManage/changeStates',
      payload: {
        visible: false,
        parent: 'ruleConfigure',
      },
    })
  }
  const onChange = (data, checked) => {
    dispatch({
      type: 'ruleManage/updateRule',
      payload: {
        id: data.id,
        formulaState: checked ? '1' : '0',
      },
    })
  }
  const columnsData = [
    {
      title: '编号',
      dataIndex: 'formulaNo',
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '规则名称',
      dataIndex: 'name',
    },
    {
      title: '规则类型',
      dataIndex: 'formulaType',
      render: (text, record) => {
        return (
          <span>{type[record.formulaType]}</span>
        )
      },
    },
    {
      title: '适用业务',
      dataIndex: 'adjustType',
      render: (text, record) => {
        return (
          <span>{adjust[record.adjustType]}</span>
        )
      },
    },
    {
      title: '计算公式',
      dataIndex: 'formula',
    },
    {
      title: '公式key',
      dataIndex: 'formulaKey',
    },
    {
      title: '公式解释',
      dataIndex: 'formulaDesc',
    },
    {
      title: '规则状态',
      dataIndex: 'formulaState',
      render: (text, record) => {
        return (
          <span>{state[record.formulaState]}</span>
        )
      },
    },
    {
      title: '是否启用',
      render: (text, record) => {
        let checked = true
        if (record.formulaState === '0') {
          checked = false
        } else {
          checked = true
        }
        return (
          <div>
            <Switch checked={checked} checkedChildren="启用" unCheckedChildren="停用" onChange={onChange.bind(this, record)} />
          </div>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/formula/queryPage',
    fields: fields || {},
    tableTime,
  }
  let modalProps = {
    dispatch,
    visible,
    adjusts,
    states,
    types,
    ruleDetail,
    hideModal,
    loading,
  }
  return (
    <div>
      <Card
        title=""
        extra={<div><Button type="primary" onClick={addRule.bind(this)}>新增</Button></div>}
      >
        <Ttable {...tableProps} />
      </Card>
      {
        visible ? <RuleModal {...modalProps} /> : null
      }
    </div>
  )
}

RuleList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  visible: PropTypes.bool,
  adjusts: PropTypes.array,
  states: PropTypes.array,
  types: PropTypes.array,
  ruleDetail: PropTypes.object,
  loading: PropTypes.isRequired,
}
export default RuleList
