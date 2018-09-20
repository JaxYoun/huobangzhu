import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import UserList from './userList'
import UserQuery from './userQuery'


const UserManagement = ({
  dispatch,
  userManagement,
}) => {
  return (
    <div>
      <UserQuery dispatch={dispatch} {...userManagement} />
      <UserList dispatch={dispatch} {...userManagement} />
    </div>
  )
}

UserManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  userManagement: PropTypes.obj,
}
export default connect(({ userManagement }) => ({ userManagement }))(UserManagement)
