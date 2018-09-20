import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Row, Button, Form, Col, Input } from 'antd'
import { regex } from 'utils'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

const CompanyInformation = ({
  dispatch,
  companyInformation,
  loading,
  form,
}) => {
  const { getFieldDecorator } = form
  const { companyDetail } = companyInformation
  const updateCompany = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.id = companyDetail.id
      dispatch({
        type: 'companyInformation/updateInfo',
        payload: formValue,
      })
    })
  }
  return (
    <div>
      <Form>
        <Card title="公司信息">
          <Row>
            <Col span={11}>
              <FormItem label="公司名称">
                {getFieldDecorator('organizationName', {
                  initialValue: companyDetail && companyDetail.organizationName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="公司地址">
                {getFieldDecorator('address', {
                  initialValue: companyDetail && companyDetail.address,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="联系方式">
          <Row>
            <Col span={11}>
              <FormItem label="联系人">
                {getFieldDecorator('linkMan', {
                  rules: [
                    {
                      required: true,
                      message: '请填写联系人',
                    },
                  ],
                  initialValue: companyDetail && companyDetail.linkMan,
                })(
                  <Input maxLength={10} />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="联系电话">
                {getFieldDecorator('telephone', {
                  rules: [
                    {
                      required: true,
                      message: '请填写联系电话',
                    },
                    { pattern: regex.telephone, message: '请填写正确的固定电话或手机号码' },
                  ],
                  initialValue: companyDetail && companyDetail.telephone,
                })(
                  <Input maxLength={20} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="公司描述">
          <Row>
            <Col>
              <FormItem label="公司描述">
                {getFieldDecorator('description', {
                  initialValue: companyDetail && companyDetail.description,
                })(
                  <TextArea rows={6} maxLength={200} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card>
        <Row className={styles.foot}>
          <Col>
            <Button type="primary" onClick={updateCompany} loading={loading.global}>确 认 修 改</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

CompanyInformation.propTypes = {
  form: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  companyInformation: PropTypes.object,
  loading: PropTypes.object,
}
const CompanyInformationForm = Form.create()(CompanyInformation)
export default connect(({ companyInformation, loading }) => ({ companyInformation, loading }))(CompanyInformationForm)
