package com.hzw.api.yreturn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.CompanyBasicInfo2;
import com.hzw.api.mapper.CompanyBasicInfoMapper2;
import com.hzw.common.exception.api.ApiException;
import com.hzw.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 改变企业代码为征信代码
 */

@WebFilter
public class HeaderFilter implements Filter {
    @Autowired
    private CompanyBasicInfoMapper2 companyBasicInfoMapper2;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    private  final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/api/v2/platform/fileUpload")));

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if (ALLOWED_PATHS.contains(requestURI)){
            chain.doFilter(servletRequest,response);
        }else {
            MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
            //获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中。
            // 在chain.doFiler方法中传递新的request对象
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {
    }

    public class MyRequestWrapper extends HttpServletRequestWrapper{

        private static final int BUFFER_SIZE = 1024 * 8;
        private final String body;

        public MyRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.body = read(request);
        }

        public String read(HttpServletRequest request) throws IOException {
            BufferedReader bufferedReader = request.getReader();
            StringWriter writer = new StringWriter();
            write(bufferedReader,writer);
            return writer.getBuffer().toString();
        }

        public long write(Reader reader, Writer writer) throws IOException {
            return write(reader, writer, BUFFER_SIZE);
        }

        public long write(Reader reader, Writer writer, int bufferSize) throws IOException
        {
            int read;
            long total = 0;
            char[] buf = new char[bufferSize];
            while( ( read = reader.read(buf) ) != -1 ) {
                writer.write(buf, 0, read);
                total += read;
            }
            return total;
        }

        public String getBody() {
            return body;
        }

        @Override
        public ServletInputStream getInputStream()  {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read(){
                    return bais.read();
                }
            };
        }

        @Override
        public BufferedReader getReader(){
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }

        /**
         * 当调用request.getHeader("token")时，则获取请求参数中token值并当做Header的值返回
         * @param name
         * @return
         */
        @Override
        public String getHeader(String name) {
            // 先从原本的Request中获取头，如果为空且名字为token，则从参数中查找并返回
            String superHeader = super.getHeader(name);
            if ("appId".equals(name)) {
                QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("appId", superHeader);
                CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
                //appId
                if (companyBasicInfo2 == null) {
                    //throw new RuntimeException("无此企业请联系管理员");
                    throw new ApiException("无此企业请联系管理员", 401);
                }
                return companyBasicInfo2.getCreditcode();
            }
            if ("appId1".equals(name)) {
                return super.getHeader("appId");
            }
            return superHeader;
        }
    }
}
