import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Button, Input } from 'antd'
import { Address, UploadImage } from 'components'
import { regex } from 'utils'
import { browserHistory } from 'react-router'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

const MyStorageEdit = ({ dispatch, myStorageEdit, form, loading }) => {
  const { getFieldDecorator } = form
  const { storageData, imgUrlSmall } = myStorageEdit
  const addressProps = {
    address: {
      key: 'address',
      value: storageData && storageData.address,
    },
    label: '仓库地址',
    lng: {
      key: 'coordX',
      value: storageData && storageData.coordX,
    },
    lat: {
      key: 'coordY',
      value: storageData && storageData.coordY,
    },
    form,
    map: true,
  }
  const save = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (storageData) {
        formValue.id = storageData.id
      }
      formValue.titleImageList = `[${formValue.titleImageList}]`
      console.log('formValue', formValue)
      dispatch({
        type: 'myStorageEdit/save',
        payload: formValue,
      })
    })
  }
  const goBack = () => {
    browserHistory.goBack()
  }
  return (
    <div>
      <Form>
        <Card title="仓储信息发布与编辑" className={styles.info}>
          <Row>
            <Col span={11}>
              <FormItem label="仓库名称">
                {getFieldDecorator('name', {
                  rules: [
                    {
                      required: true,
                      message: '请输入仓库名称',
                    },
                  ],
                  initialValue: storageData && storageData.name,
                })(
                  <Input disabled={storageData && storageData.lifecycle !== 0} />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="仓库容量">
                {getFieldDecorator('capacity', {
                  rules: [
                    {
                      required: true,
                      message: '请输入仓库容量',
                    },
                    { pattern: regex.number, message: '请输入正确的容量' },
                  ],
                  initialValue: storageData && `${storageData.capacity}`,
                })(<Input addonAfter={<span>m²</span>} disabled={storageData && storageData.lifecycle !== 0} />)}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="租赁价格">
                {getFieldDecorator('unitPrice', {
                  rules: [
                    {
                      required: true,
                      message: '请输入仓库租赁价格',
                    },
                    { pattern: regex.number, message: '请输入正确的价格' },
                  ],
                  initialValue: storageData && `${storageData.unitPrice}`,
                })(<Input addonAfter={<span>元/m²</span>} disabled={storageData && storageData.lifecycle !== 0} />)}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="租赁时间">
                {getFieldDecorator('minRentTime', {
                  rules: [
                    {
                      required: true,
                      message: '请输入仓库租赁时长',
                    },
                    { pattern: regex.number, message: '请输入正确的时长' },
                  ],
                  initialValue: storageData && `${storageData.minRentTime}`,
                })(<Input addonAfter={<span>月</span>} disabled={storageData && storageData.lifecycle !== 0} />)}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <Col span={24}>
                <FormItem label="联系人">
                  {getFieldDecorator('ownerName', {
                    rules: [
                      {
                        required: true,
                        message: '请输入联系人',
                      },
                    ],
                    initialValue: storageData && storageData.ownerName,
                  })(<Input disabled={storageData && storageData.lifecycle !== 0} />)}
                </FormItem>
              </Col>
              <Col span={24}>
                <FormItem label="联系电话">
                  {getFieldDecorator('telephone', {
                    rules: [
                      {
                        required: true,
                        message: '请输入联系人电话',
                      },
                      { pattern: regex.phone, message: '请输入正确的电话号码' },
                    ],
                    initialValue: storageData && storageData.telephone,
                  })(<Input disabled={storageData && storageData.lifecycle !== 0} />)}
                </FormItem>
              </Col>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="仓库描述">
                {getFieldDecorator('warehouseDescribe', {
                  rules: [
                    {
                      required: true,
                      message: '请输入仓库描述',
                    },
                  ],
                  initialValue: storageData && storageData.warehouseDescribe,
                })(
                  <TextArea
                    autosize={{ minRows: 6, maxRows: 18 }}
                    placeholder="仓储描述信息"
                    disabled={storageData && storageData.lifecycle !== 0}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="头像设置">
                {getFieldDecorator('titleImageList', {
                  initialValue: imgUrlSmall || (storageData && storageData.titleImageList[0]),
                  rules: [{ required: true, message: '请选择图片!' }],
                })(
                  <Input style={{ display: 'none' }} />
                )}
                <UploadImage form={form}
                  imgUrl={imgUrlSmall || (storageData && storageData.titleImageList[0])}
                  dispatch={dispatch}
                  type="myStorageEdit/changeStates"
                  name="imgUrlSmall"
                  nameForm="titleImageList"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <Address {...addressProps} disabled={storageData && storageData.lifecycle !== 0} />
            </Col>
          </Row>
        </Card>
        <Card>
          <Row className={styles.foot}>
            <Col>
              {
                !storageData || storageData.lifecycle === 0 ? (
                  <Button type="primary" onClick={save} loading={loading.global}>
                    确 认 保 存
                  </Button>
                ) : null
              }
              {
                storageData ? (
                  <Button type="primary" onClick={goBack}>返 回</Button>
                ) : null
              }
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}

MyStorageEdit.propTypes = {
  form: PropTypes.isRequired,
  myStorageEdit: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  loading: PropTypes.isRequired,
}

const MyStorageEditForm = Form.create()(MyStorageEdit)
export default connect(({ myStorageEdit, loading }) => ({ myStorageEdit, loading }))(
  MyStorageEditForm
)
