import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button } from 'antd'
import { UploadImage } from 'components'
import styles from './index.less'

const FormItem = Form.Item

const PersonalInfo = ({
  dispatch,
  personalInfo,
  form,
  loading,
}) => {
  const { getFieldDecorator } = form
  const { userDetail, imgUrlSmall } = personalInfo
  const changeImage = () => {
    const value = form.getFieldsValue(['imageUrl'])
    dispatch({
      type: 'personalInfo/changeImage',
      payload: {
        imageUrl: value.imageUrl,
      },
    })
  }
  return (
    <div>
      <Card title="个人信息" className={styles.info}>
        <Row>
          <Col>
            <FormItem label="昵称">
              {getFieldDecorator('nickName', {
                initialValue: userDetail && userDetail.nickName,
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="手机号">
              {getFieldDecorator('telephone', {
                initialValue: userDetail && userDetail.telephone,
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <FormItem label="头像设置">
            {getFieldDecorator('imageUrl', {
              initialValue: imgUrlSmall || userDetail.imageUrl,
              rules: [{ required: true, message: '请选择图片!' }],
            })(
              <Input style={{ display: 'none' }} />
            )}
            <UploadImage form={form}
              imgUrl={imgUrlSmall || userDetail.imageUrl}
              dispatch={dispatch}
              type="personalInfo/changeStates"
              name="imgUrlSmall"
              nameForm="imageUrl"
            />
          </FormItem>
        </Row>
      </Card>
      <Card>
        <Row className={styles.foot}>
          <Col>
            <Button type="primary" onClick={changeImage} loading={loading.global}>确 认</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

PersonalInfo.propTypes = {
  personalInfo: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.isRequired,
  loading: PropTypes.object,
}
const PersonalInfoForm = Form.create()(PersonalInfo)
export default connect(({ personalInfo, loading }) => ({ personalInfo, loading }))(PersonalInfoForm)
