import { login, userPost, selectPost } from '../services/login'

export default {
  namespace: 'login',
  state: {
    loginLoading: false,
    visible: false,
    posts: [],
    selectedPostId: '',
  },

  effects: {
    * login ({
      payload,
    }, { put, call }) {
      yield put({ type: 'showLoginLoading' })
      sessionStorage.isvisited = 'yes'
      const data = yield call(login, payload)
      yield put({ type: 'hideLoginLoading' })
      if (data.success) {
        const userData = yield call(userPost)
        if (userData.data.posts.length === 1) {
          const postData = yield call(selectPost, { postId: userData.data.posts[0].postId })
          if (postData.success) {
            yield put({ type: 'app/query' })
          }
        } else if (userData.data.posts.length > 1) {
          yield put({ type: 'showPosts', payload: userData.data.posts })
        } else {
          const errorMessage = { message: '用户岗位信息错误！请联系管理员' }
          throw errorMessage
        }
      }
    },
    * handlePostModal ({
      payload,
    }, { put, call }) {
      if (payload.postId) {
        const postData = yield call(selectPost, payload)
        if (postData.success && postData.data) {
          sessionStorage.postName = payload.postName
          yield put({ type: 'closeModal', payload: { visible: false } })
          yield put({ type: 'app/query' })
        } else {
          const errorMessage = { message: '当前岗位不能登录，请联系管理员' }
          throw errorMessage
        }
      } else {
        yield put({ type: 'closeModal', payload })
      }
    },
  },
  reducers: {
    showLoginLoading (state) {
      return {
        ...state,
        loginLoading: true,
      }
    },
    hideLoginLoading (state) {
      return {
        ...state,
        loginLoading: false,
      }
    },
    closeModal (state, action) {
      return {
        ...state,
        ...action.payload,
      }
    },
    showPosts (state, action) {
      return {
        ...state,
        visible: true,
        posts: action.payload,
      }
    },
  },
}
