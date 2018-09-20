import React from 'react'
import { Card, Switch } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import moment from 'moment'
import styles from '../common/index.less'
import BannerModalForm from './bannerModal'

const BannerList = ({ dispatch, fields, tableTime, bannerModal, locationData, skipTypeData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'adManagement/getBannerData', payload: { id: data.id } })
    dispatch({ type: 'adManagement/updateBannerModal', payload: { visible: true } })
  }
  // 启用禁用
  const onChange = (data, checked) => {
    if (checked) {
      dispatch({ type: 'adManagement/updateBannerstatus', payload: { id: data.id, ifEnable: '1' } })
    } else {
      dispatch({ type: 'adManagement/updateBannerstatus', payload: { id: data.id, ifEnable: '0' } })
    }
  }
  console.log('bannerModal', bannerModal, bannerModal.visible)
  const columnsData = [
    {
      title: '编号',
      dataIndex: 'code',
      width: 80,
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: 'Banner名称',
      dataIndex: 'name',
      width: 120,
    },
    {
      title: 'Banner图片',
      dataIndex: 'imagePath',
      width: 100,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.imagePath} /> </div>
        )
      },
    },
    {
      title: 'Banner位置',
      dataIndex: 'location.name',
      width: 80,
    },
    {
      title: '跳转方式',
      dataIndex: 'skipType.name',
      width: 80,
    },
    {
      title: '排序',
      dataIndex: 'sortNo',
      width: 80,
    },
    {
      title: 'Banner状态',
      dataIndex: 'ifEnable',
      width: 80,
      render: (text, record) => {
        if (record.ifEnable === '0') {
          text = '已禁用'
        } else {
          text = '可用'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '跳转URL地址 ',
      dataIndex: 'url',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 300,
    },
    {
      title: '是否启用',
      render: (text, record) => {
        let checked = true
        if (record.ifEnable === '0') {
          checked = false
        } else {
          checked = true
        }
        return (
          <div>
            <Switch checked={checked} checkedChildren="启用" unCheckedChildren="禁用" onChange={onChange.bind(this, record)} />
          </div>
        )
      },
      width: 120,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/banner/getBannerListByPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1300 }}
      />
      {bannerModal.visible && <BannerModalForm {...bannerModal} locationData={locationData} dispatch={dispatch} skipTypeData={skipTypeData} />}
    </Card>
  )
}
BannerList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  bannerModal: PropTypes.obj,
  locationData: PropTypes.array,
  skipTypeData: PropTypes.array,
}
export default BannerList
