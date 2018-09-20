import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import BrandAddModalForm from './brandAdd'
import styles from '../common/index.less'

const BrandList = ({ dispatch, fields, tableTime, brandModal }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'basicData/updateModalProvince', payload: { parentId: 0 } })
    if (data.area.level1AreaCode) {
      dispatch({ type: 'basicData/updateModalFromCity', payload: { parentId: data.area.level1id } })
    }
    if (data.area.level2AreaCode) {
      dispatch({ type: 'basicData/updateModalFromCounty', payload: { parentId: data.area.level2id } })
    }
    console.log('brandModal', brandModal)
    dispatch({ type: 'basicData/updateBrandModal', payload: { visible: true, firmData: data } })
  }
  // 删除
  const deleteBrand = (data) => {
    dispatch({ type: 'basicData/deleteBrand', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '品牌编号',
      dataIndex: 'brandNo',
      width: 180,
    },
    {
      title: '品牌名称',
      dataIndex: 'name',
      width: 180,
    },
    {
      title: '品牌图片',
      dataIndex: 'headerBit',
      width: 180,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.headerBit} alt="" /> </div>
        )
      },
    },
    {
      title: '品牌所在地',
      dataIndex: 'startTime',
      width: 180,
      render: (text, record) => {
        return (
          <div>{record.area.level1Name}{record.area.level2Name}{record.area.level3Name}</div>
        )
      },
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            {/* <Popconfirm title="确定要删除吗？" onConfirm={deleteBrand.bind(this, record)}>
              <Button>删除</Button>
            </Popconfirm> */}
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/brand/queryPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps} />
        {brandModal.visible && <BrandAddModalForm {...brandModal} dispatch={dispatch} />}
    </Card>
  )
}
BrandList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  brandModal: PropTypes.object,
}
export default BrandList
