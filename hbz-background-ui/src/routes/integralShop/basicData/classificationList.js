import React from 'react'
import { connect } from 'dva'
import { Card, Button, Popconfirm } from 'antd'
import moment from 'moment'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from '../common/index.less'
import ClassAddModalForm from './classificationAdd'

const ClassificationList = ({ dispatch, fields, tableTime, classificationModal, wareTypeData }) => {

  // 打开模态框
  const showModal = (data) => {
    // 获取树
    dispatch({ type: 'basicData/getTreeData', payload: { } })
    dispatch({ type: 'basicData/updateClassificationModal', payload: { visible: true, firmData: data } })
  }
  // 删除游戏
  const deleteGame = (data) => {
    dispatch({ type: 'basicData/deleteClass', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '分类编号',
      dataIndex: 'typeNo',
      width: 180,
    },
    {
      title: '分类名称',
      dataIndex: 'name',
      width: 180,
    },
    {
      title: '分类图标',
      dataIndex: 'headerBit',
      width: 180,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.headerBit} alt="" /> </div>
        )
      },
    },
    {
      title: '分类级别',
      dataIndex: 'level',
      width: 180,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteGame.bind(this, record)}>
              <Button>删除</Button>
            </Popconfirm>
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/wareType/queryPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps} />
      {classificationModal.visible && <ClassAddModalForm {...classificationModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
    </Card>
  )
}
ClassificationList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  classificationModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
}
export default ClassificationList
