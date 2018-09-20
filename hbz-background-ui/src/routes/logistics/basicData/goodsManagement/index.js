import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import GoodsList from './goodsList'
import GoodsQuery from './goodsQuery'


const GoodsManagement = ({
  dispatch,
  goodsManagement,
}) => {
  return (
    <div>
      <GoodsQuery dispatch={dispatch} {...goodsManagement} />
      <GoodsList dispatch={dispatch} {...goodsManagement} />
    </div>
  )
}

GoodsManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  goodsManagement: PropTypes.obj,
}
export default connect(({ goodsManagement }) => ({ goodsManagement }))(GoodsManagement)
